import base64
import os
import numpy as np
from utilss import EventMsg, logger
from dotenv import load_dotenv
import json
import cv2
import pika
import torch
import requests
import function.utils_rotate as utils_rotate
import function.helper as helper
import threading


load_dotenv(".env")

yolo_LP_detect = torch.hub.load(
    "yolov5", "custom", path="model/LP_detector.pt", force_reload=True, source="local"
)
yolo_license_plate = torch.hub.load(
    "yolov5", "custom", path="model/LP_ocr.pt", force_reload=True, source="local"
)


def detectPlate(img):
    plates = yolo_LP_detect(img, size=640)
    list_plates = plates.pandas().xyxy[0].values.tolist()
    list_read_plates = set()
    if len(list_plates) == 0:
        lp = helper.read_plate(yolo_license_plate, img)
        if lp != "unknown":
            cv2.putText(
                img, lp, (7, 70), cv2.FONT_HERSHEY_SIMPLEX, 0.9, (36, 255, 12), 2
            )
            list_read_plates.add(lp)
    else:
        for plate in list_plates:
            flag = 0
            x = int(plate[0])  # xmin
            y = int(plate[1])  # ymin
            w = int(plate[2] - plate[0])  # xmax - xmin
            h = int(plate[3] - plate[1])  # ymax - ymin
            crop_img = img[y : y + h, x : x + w]
            cv2.rectangle(
                img,
                (int(plate[0]), int(plate[1])),
                (int(plate[2]), int(plate[3])),
                color=(0, 0, 225),
                thickness=2,
            )
            lp = ""
            for cc in range(0, 2):
                for ct in range(0, 2):
                    lp = helper.read_plate(
                        yolo_license_plate, utils_rotate.deskew(crop_img, cc, ct)
                    )
                    if lp != "unknown":
                        list_read_plates.add(lp)
                        cv2.putText(
                            img,
                            lp,
                            (int(plate[0]), int(plate[1] - 10)),
                            cv2.FONT_HERSHEY_SIMPLEX,
                            0.9,
                            (36, 255, 12),
                            2,
                        )
                        flag = 1
                        break
                if flag == 1:
                    break
    result = []
    for p in list_read_plates:
        result.append(p)
    return result, img


def checkPlate(frameDecoded, event: EventMsg):
    logger.info("[checkPlate] thread is running...")
    listReadPlates, imageLabeled = detectPlate(frameDecoded)
    if len(listReadPlates) > 0:
        logger.info("[checkPlate] plate detected: %s", listReadPlates)
        _, imgEncoded = cv2.imencode(".jpg", imageLabeled)
        file = {
            "image": (
                "image.jpg",
                imgEncoded.tobytes(),
                "image/jpeg",
                {"Expires": "0"},
            ),
            "data": (
                None,
                json.dumps({"plates": listReadPlates, "bin_id": event.binId}),
                "application/json",
            ),
        }
        response = requests.post(
            config.garbageServiceUrl + "/trucks/checkPlate",
            files=file,
            headers={"X-API-KEY": config.internalApiKey},
        )
        logger.info("[consumer] call api checkPlate response: %s", response.json())


# consumer
class RabbitmqClient:
    def __init__(self, host: str, port: int, username: str, password: str) -> None:
        self.connection = pika.BlockingConnection(
            pika.ConnectionParameters(
                host=host,
                port=port,
                connection_attempts=3,
                credentials=pika.PlainCredentials(username, password),
            )
        )
        self.channel = self.connection.channel()

        self.channel.exchange_declare(
            exchange=config.rabbitmqExchange, exchange_type=config.rabbitmqExchangeType
        )
        result = self.channel.queue_declare(queue=config.rabbitmqQueue, exclusive=True)
        queueName = result.method.queue
        self.channel.queue_bind(exchange=config.rabbitmqExchange, queue=queueName)

    def close(self):
        if self.connection:
            self.connection.close()

        if self.channel:
            self.channel.close()


class ConfigValue:
    def __init__(self) -> None:
        self.binId: str
        self.cameraUrl: str
        self.scheduleInSecs: int
        self.rabbitmqHost: str
        self.rabbitmqPort: int
        self.rabbitmqUser: str
        self.rabbitmqPwd: str
        self.rabbitmqExchange: str
        self.rabbitmqExchangeType: str
        self.rabbitmqQueue: str
        self.garbageServiceUrl: str
        self.internalApiKey: str


def readEnv() -> ConfigValue:
    config = ConfigValue()
    config.rabbitmqHost = os.getenv("RABBITMQ_HOST", "localhost")
    config.rabbitmqPort = int(os.getenv("RABBITMQ_PORT", 5672))
    config.rabbitmqUser = os.getenv("RABBITMQ_USER", "guest")
    config.rabbitmqPwd = os.getenv("RABBITMQ_PWD", "guest")
    config.rabbitmqExchange = os.getenv("RABBITMQ_EXCHANGE_CAMERA", "camera")
    config.rabbitmqExchangeType = os.getenv("RABBITMQ_EXCHANGE_TYPE_CAMERA", "fanout")
    config.rabbitmqQueue = os.getenv("RABBITMQ_QUEUE_VEHICLE_DETECT", "vehicle")
    config.garbageServiceUrl = os.getenv(
        "GARBAGE_SERVICE_URL", "http://localhost:8081/garbage-service"
    )
    config.internalApiKey = os.getenv("INTERNAL_API_KEY", "")

    logger.info("[readEnv] config value: %s", json.dumps(config.__dict__))
    return config


def consumer(ch, method, properties, body):
    logger.info("[consumer] received message")
    event = json.loads(body)
    event = EventMsg(**event)
    frame = base64.b64decode(event.frame)
    frameDecoded = cv2.imdecode(np.frombuffer(frame, np.uint8), cv2.IMREAD_COLOR)
    t = threading.Thread(
        target=checkPlate,
        args=(
            frameDecoded,
            event,
        ),
    )
    t.start()


if __name__ == "__main__":
    config = readEnv()
    rabbitmqClient = RabbitmqClient(
        host=config.rabbitmqHost,
        port=config.rabbitmqPort,
        username=config.rabbitmqUser,
        password=config.rabbitmqPwd,
    )

    rabbitmqClient.channel.basic_consume(
        queue=config.rabbitmqQueue, on_message_callback=consumer, auto_ack=True
    )
    rabbitmqClient.channel.start_consuming()
    rabbitmqClient.close()

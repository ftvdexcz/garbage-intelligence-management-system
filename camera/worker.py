import base64
import os
import time
from utils import EventMsg, logger
from dotenv import load_dotenv
import json
import cv2
import pika

load_dotenv(".env")


# producer
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


def captureFrame(config: ConfigValue, rabbitmqClient: RabbitmqClient) -> None:
    while True:
        previousTime = 0
        cap = cv2.VideoCapture(config.cameraUrl)

        if not cap.isOpened():
            logger.error(
                "[captureFrame] open video capture failed, restarting in 10s..."
            )
            cap.release()
            time.sleep(10)
            continue

        while cap.isOpened():
            try:
                ret, frame = cap.read()

                if ret:
                    curTimestamp = time.time()

                    if curTimestamp - previousTime >= config.scheduleInSecs:
                        previousTime = curTimestamp
                        logger.info("[captureFrame] fire event to rabbitmq")

                        _, imgEncoded = cv2.imencode(".jpg", frame)
                        imageBase64 = base64.b64encode(imgEncoded.tobytes()).decode(
                            "utf-8"
                        )
                        eventMsg = EventMsg(
                            config.binId, int(curTimestamp * 1000), imageBase64
                        )

                        rabbitmqClient.channel.basic_publish(
                            exchange=config.rabbitmqExchange,
                            routing_key="",
                            body=json.dumps(eventMsg.__dict__),
                        )
                else:
                    logger.error(
                        "[captureFrame] open video capture failed, restarting in 10s..."
                    )
                    cap.release()
                    time.sleep(10)
                    break
            except Exception as e:
                logger.error("[captureFrame] error: %s", e)


def readEnv() -> ConfigValue:
    config = ConfigValue()
    config.binId = os.getenv("BIN_ID", "")
    config.cameraUrl = os.getenv("CAMERA_URL", "")
    config.scheduleInSecs = int(os.getenv("SCHEDULE_IN_SECS", 10))
    config.rabbitmqHost = os.getenv("RABBITMQ_HOST", "localhost")
    config.rabbitmqPort = int(os.getenv("RABBITMQ_PORT", 5672))
    config.rabbitmqUser = os.getenv("RABBITMQ_USER", "guest")
    config.rabbitmqPwd = os.getenv("RABBITMQ_PWD", "guest")
    config.rabbitmqExchange = os.getenv("RABBITMQ_EXCHANGE_CAMERA", "camera")
    config.rabbitmqExchangeType = os.getenv("RABBITMQ_EXCHANGE_TYPE_CAMERA", "fanout")

    logger.info("[readEnv] config value: %s", json.dumps(config.__dict__))
    return config


if __name__ == "__main__":
    config = readEnv()
    rabbitmqClient = RabbitmqClient(
        host=config.rabbitmqHost,
        port=config.rabbitmqPort,
        username=config.rabbitmqUser,
        password=config.rabbitmqPwd,
    )
    captureFrame(config, rabbitmqClient)
    rabbitmqClient.close()

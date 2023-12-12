import base64
import os
import time

import numpy as np
from utils import EventMsg, logger
from dotenv import load_dotenv
import json
import cv2
import pika

load_dotenv(".env")


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


def readEnv() -> ConfigValue:
    config = ConfigValue()
    config.rabbitmqHost = os.getenv("RABBITMQ_HOST", "localhost")
    config.rabbitmqPort = int(os.getenv("RABBITMQ_PORT", 5672))
    config.rabbitmqUser = os.getenv("RABBITMQ_USER", "guest")
    config.rabbitmqPwd = os.getenv("RABBITMQ_PWD", "guest")
    config.rabbitmqExchange = os.getenv("RABBITMQ_EXCHANGE_CAMERA", "camera")
    config.rabbitmqExchangeType = os.getenv("RABBITMQ_EXCHANGE_TYPE_CAMERA", "fanout")
    config.rabbitmqQueue = os.getenv("RABBITMQ_QUEUE_VEHICLE_DETECT", "vehicle")

    logger.info("[readEnv] config value: %s", json.dumps(config.__dict__))
    return config


def consumer(ch, method, properties, body):
    logger.info("[consumer] received message")
    event = json.loads(body)
    event = EventMsg(**event)
    frame = base64.b64decode(event.frame)
    frameDecoded = cv2.imdecode(np.frombuffer(frame, np.uint8), cv2.IMREAD_COLOR)
    cv2.imshow("Received Image", frameDecoded)
    cv2.waitKey(0)
    cv2.destroyAllWindows()


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

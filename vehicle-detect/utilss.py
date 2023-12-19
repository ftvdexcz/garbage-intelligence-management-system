import logging

# Cấu hình logging
logging.basicConfig(
    level=logging.INFO,  # (DEBUG, INFO, WARNING, ERROR, CRITICAL)
    format="%(asctime)s - %(levelname)s - %(message)s",
    # filename="app.log",
    # filemode="w",
)

logger = logging.getLogger("logger")


class EventMsg(object):
    def __init__(self, binId: str, timestamp: int, frame: str) -> None:
        self.binId = binId
        self.timestamp = timestamp
        self.frame = frame

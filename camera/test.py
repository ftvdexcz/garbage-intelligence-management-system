import json
import cv2, requests

frame = cv2.imread(
    "/home/longdq/Downloads/photo_2023-12-19_15-46-43.jpg", cv2.IMREAD_COLOR
)
_, imgEncoded = cv2.imencode(".jpg", frame)
data = {"plates": []}
file = {
    "image": ("image.jpg", imgEncoded.tostring(), "image/jpeg", {"Expires": "0"}),
    "data": (None, json.dumps(data), "application/json"),
}

response = requests.post(
    "http://localhost:8081/garbage-service" + "/trucks/checkPlate",
    # data={"data": str(json.dumps(data))},
    files=file,
    headers={
        "X-API-KEY": "lJHqePHhgVPHOiwSvsDSxGTgHJSOfksqHQNZLzHFDHlVyhlWmBEbQdynAsiMSWLbNlOBifjOrbqSvrAcKmxfBUAyJZZcJjMrlGRABfndveWnYcuRsnxTcCRANQNyhoQA"
    },
)
print("[consumer] call api checkPlate response: ", response.json())

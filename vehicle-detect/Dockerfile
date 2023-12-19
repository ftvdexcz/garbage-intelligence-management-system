FROM python:3.10-slim

WORKDIR /app

COPY worker.py .
COPY yolov5 yolov5
COPY model model
COPY function function
COPY .env .
COPY utilss.py .
COPY requirements.txt .

RUN pip install -r requirements.txt

CMD ["python", "worker.py"]
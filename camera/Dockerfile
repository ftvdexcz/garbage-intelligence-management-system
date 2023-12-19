FROM python:3.8-slim

WORKDIR /app

COPY worker.py .
COPY utils.py .
COPY requirements.txt .
COPY .env .

RUN pip install -r requirements.txt

CMD ["python", "worker.py"]
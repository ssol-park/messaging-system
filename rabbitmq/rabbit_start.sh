#!/bin/bash

# 스크립트 실행 중 오류 발생 시 중단
echo "Starting the RabbitMQ build and deploy process..."
set -e

# 기존 RabbitMQ 컨테이너 중지 및 제거
echo "Stopping and removing existing RabbitMQ containers..."
docker compose down

# RabbitMQ 네트워크 생성 (이미 존재하면 무시됨)
echo "Ensuring RabbitMQ network exists..."
docker network create rabbitmq_net || true

# RabbitMQ 컨테이너만 빌드 및 실행
echo "Building and starting RabbitMQ containers..."
docker compose up --build -d

# 배포 완료
echo "RabbitMQ build and deploy process completed successfully."

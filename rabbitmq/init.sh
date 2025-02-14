#!/bin/bash

# 스크립트 실행 중 오류 발생 시 중단
echo "Starting the build and deploy process..."
set -e

# 기존 RabbitMQ 컨테이너 중지 및 제거
echo "Stopping and removing RabbitMQ containers..."
docker compose stop rabbitmq1 rabbitmq2 rabbitmq3
docker compose rm -f rabbitmq1 rabbitmq2 rabbitmq3

# RabbitMQ 컨테이너만 빌드 및 실행
echo "Building and starting RabbitMQ containers..."
docker compose up --build -d rabbitmq1 rabbitmq2 rabbitmq3

# 배포 완료
echo "Build and deploy process completed successfully."

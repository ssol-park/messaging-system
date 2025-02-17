#!/bin/bash

# Gradle 빌드
echo "Running Gradle build..."
./gradlew clean build

# 기존 컨테이너 중지 및 삭제 (존재하지 않아도 에러 없이 진행)
if docker ps -a | grep -q "rabbit_app"; then
    echo "Stopping and removing the existing application container..."
    docker stop rabbit_app
    docker rm rabbit_app
else
    echo "No existing application container found."
fi

# 기존 이미지 삭제 (존재하지 않아도 에러 없이 진행)
if docker images | grep -q "rabbit_app"; then
    echo "Removing the old application image..."
    docker rmi rabbit_app
else
    echo "No existing application image found."
fi

# 새로운 애플리케이션 이미지 빌드
echo "Building the new application image..."
docker build -t rabbit_app .

# 새로운 컨테이너 실행 (백그라운드)
echo "Starting the application container..."
docker run --rm --name rabbit_app \
  --network rabbitmq_rabbitmq_net \
  -e SPRING_RABBITMQ_ADDRESSES=rabbitmq1:5672,rabbitmq2:5672,rabbitmq3:5672 \
  -p 8080:8080 \
  -d rabbit_app

echo "Application build and deploy process completed successfully."

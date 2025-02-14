#!/bin/bash

echo "Starting the build and deploy process..."
set -e  # 오류 발생 시 즉시 중단

# Gradle 빌드
echo "Running Gradle build..."
./gradlew clean build

# 기존 애플리케이션 컨테이너만 중지 및 제거
echo "Stopping and removing the application container..."
docker compose stop rabbit_app
docker compose rm -f rabbit_app

# 애플리케이션 컨테이너만 다시 빌드 및 실행
echo "Rebuilding and starting the application container..."
docker compose up --build -d rabbit_app

echo "Build and deploy process completed successfully."

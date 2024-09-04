#!/bin/bash

# 주키퍼 ID 설정
echo "${ZOO_MY_ID}" > /opt/zookeeper/data/myid

# 주키퍼 시작
/opt/zookeeper/bin/zkServer.sh start-foreground &

# ZooKeeper 포트가 열릴 때까지 대기
while ! nc -z localhost 2181; do
  sleep 1
done

# 카프카 설정 파일 수정
sed -i 's/zookeeper.connect=localhost:2181/zookeeper.connect=broker01:2181,broker02:2181,broker03:2181/' /opt/kafka/config/server.properties
sed -i 's/#listeners=PLAINTEXT:\/\/:9092/listeners=PLAINTEXT:\/\/0.0.0.0:9092/' /opt/kafka/config/server.properties

# 실제 IP 주소 가져오기
HOST_IP=$(hostname -I | awk '{print $1}')

# advertised.listeners 설정 추가
echo "advertised.listeners=PLAINTEXT://${HOST_IP}:9092" >> /opt/kafka/config/server.properties

# 카프카 시작
/opt/kafka/bin/kafka-server-start.sh /opt/kafka/config/server.properties

# 베이스 이미지 설정
FROM openjdk:17-jdk-alpine

# 필요한 패키지 설치
RUN apk --no-cache add wget bash tar gzip

# 환경 변수 설정
ENV ZOOKEEPER_VERSION 3.8.1
ENV KAFKA_VERSION 3.5.0
ENV SCALA_VERSION 2.13

# 주키퍼 설치
RUN wget -O /tmp/zookeeper.tar.gz https://archive.apache.org/dist/zookeeper/zookeeper-$ZOOKEEPER_VERSION/apache-zookeeper-$ZOOKEEPER_VERSION-bin.tar.gz \
    && mkdir -p /opt/zookeeper \
    && tar -xzf /tmp/zookeeper.tar.gz -C /opt/zookeeper --strip-components=1 \
    && rm /tmp/zookeeper.tar.gz

# 카프카 설치
RUN wget -O /tmp/kafka.tgz https://archive.apache.org/dist/kafka/$KAFKA_VERSION/kafka_$SCALA_VERSION-$KAFKA_VERSION.tgz \
    && mkdir -p /opt/kafka \
    && tar -xzf /tmp/kafka.tgz -C /opt/kafka --strip-components=1 \
    && rm /tmp/kafka.tgz

# 작업 디렉토리 설정
WORKDIR /opt/zookeeper

# 설정 파일 복사
COPY zoo.cfg /opt/zookeeper/conf/zoo.cfg
COPY start.sh /opt/start.sh
RUN chmod +x /opt/start.sh

# 포트 설정
EXPOSE 2181 2888 3888 9092

# 주키퍼 및 카프카 실행
CMD ["/opt/start.sh"]

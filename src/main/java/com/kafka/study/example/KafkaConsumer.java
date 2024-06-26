package com.kafka.study.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.group-id}")
    public void listen(String message,
                       @Header(KafkaHeaders.RECEIVED_PARTITION)  int partition,
                       @Header(KafkaHeaders.OFFSET)  int offset) {
        log.info("Received message: {}, partition: {}, offset: {}", message, partition, offset);
    }
}

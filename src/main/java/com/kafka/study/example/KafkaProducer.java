package com.kafka.study.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducer {
    @Value("${kafka.topic.name}")
    private String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send(topic, message)
                .thenAccept(result -> {
                    RecordMetadata metadata = result.getRecordMetadata();
                    log.info("Sent message:'{}' to topic:{} partition:{} offset:{}", message, metadata.topic(), metadata.partition(), metadata.offset());
                })
                .exceptionally(err -> {
                    log.error("Failed to send message: '{}'", message, err);
                    return null;
                });
    }
}

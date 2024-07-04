package com.kafka.study.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class KafkaProducer {

    @Value("${kafka.psr-topic.name}")
    private String psrTopic;

    @Value("${kafka.raw-scrap-data.name}")
    private String rawScrapData;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ScrapService scrapService;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ScrapService scrapService) {
        this.kafkaTemplate = kafkaTemplate;
        this.scrapService = scrapService;
    }

    public void sendMessage(String message) {
        sendMessage(psrTopic, message);
    }

    public void sendMessage(String topic, String message) {
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

    public void sendRawScrapData() {
        sendMessage(rawScrapData, scrapService.getJsonData());
    }
}

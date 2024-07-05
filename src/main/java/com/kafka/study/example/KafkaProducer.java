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

    @Value("${kafka.topics.raw-scrap-data}")
    private String rawScrapDataTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ScrapService scrapService;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ScrapService scrapService) {
        this.kafkaTemplate = kafkaTemplate;
        this.scrapService = scrapService;
    }

    public void scrapData(String postId) {
        kafkaTemplate.send(rawScrapDataTopic, postId).thenAccept(result -> {
                    RecordMetadata metadata = result.getRecordMetadata();
                    log.info("Sent uri:'{}' to topic:{} partition:{} offset:{}", postId, metadata.topic(), metadata.partition(), metadata.offset());
                })
                .exceptionally(err -> {
                    log.error("Failed to send uri: '{}'", postId, err);
                    return null;
                });
    }
}

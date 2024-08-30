package com.message.study.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducer {

    @Value("${kafka.topics.raw-scrap-data}")
    private String rawScrapDataTopic;
    @Value("${kafka.topics.processed-data}")
    private String processedDataTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void scrapData(String postId) {
        kafkaTemplate.send(rawScrapDataTopic, postId).thenAccept(result -> {
                    RecordMetadata metadata = result.getRecordMetadata();
                    log.info("Sent uri:'{}' to topic:{} partition:{} offset:{}", postId, metadata.topic(), metadata.partition(), metadata.offset());
                })
                .exceptionally(err -> {
                    log.error("Failed to send uri: '{}'", err);
                    return null;
                });
    }

    public void processData(String html) {
        // TODO :: Document 객체를 어디서 어떻게 처리 할 지 고민 필요. Serializer/Deserializer
        kafkaTemplate.send(processedDataTopic, html).thenAccept(result -> {
                    RecordMetadata metadata = result.getRecordMetadata();
                    log.info("Sent uri:'{}' to topic:{} partition:{} offset:{}", metadata.topic(), metadata.partition(), metadata.offset());
                })
                .exceptionally(err -> {
                    log.error("Failed to send uri: '{}'", err);
                    return null;
                });
    }
}

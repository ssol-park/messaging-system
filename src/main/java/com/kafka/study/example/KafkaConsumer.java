package com.kafka.study.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class KafkaConsumer {

    private final String instanceId = UUID.randomUUID().toString();
    private final KafkaProducer kafkaProducer;
    private final ScrapService scrapService;

    public KafkaConsumer(KafkaProducer kafkaProducer, ScrapService scrapService) {
        this.kafkaProducer = kafkaProducer;
        this.scrapService = scrapService;
    }

    @KafkaListener(topics = "${kafka.topics.raw-scrap-data}", groupId = "${kafka.consumer-groups.raw-scrap-group}")
    public void rawScrapDataListen(String postId,
                       @Header(KafkaHeaders.RECEIVED_PARTITION)  int partition,
                       @Header(KafkaHeaders.OFFSET)  int offset) {

        Document doc = scrapService.getScrapData(postId);
        log.info("[rawScrapDataListen] instanceId:{}, Received message: {}, partition: {}, offset: {}", instanceId, doc.text(), partition, offset);

        kafkaProducer.processData(doc);
    }

    @KafkaListener(topics = "${kafka.topics.processed-data}", groupId = "${kafka.consumer-groups.processed-data-group}")
    public void processedDataListen(String data,
                                   @Header(KafkaHeaders.RECEIVED_PARTITION)  int partition,
                                   @Header(KafkaHeaders.OFFSET)  int offset) {

        log.info("[processedDataListen] instanceId:{}, partition: {}, offset: {} data:{}", instanceId, partition, offset, data);

        // TODO :: 가공 로직
    }
}




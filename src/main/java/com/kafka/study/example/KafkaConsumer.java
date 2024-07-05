package com.kafka.study.example;

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
    private final ScrapService scrapService;

    public KafkaConsumer(ScrapService scrapService) {
        this.scrapService = scrapService;
    }

    @KafkaListener(topics = "${kafka.topics.raw-scrap-data}", groupId = "${kafka.consumer-groups.raw-scrap-group}")
    public void rawScrapDataListen(String postId,
                       @Header(KafkaHeaders.RECEIVED_PARTITION)  int partition,
                       @Header(KafkaHeaders.OFFSET)  int offset) {
        Document doc = scrapService.getScrapData(postId);
        log.info("[rawScrapDataListen] instanceId:{}, Received message: {}, partition: {}, offset: {}", instanceId, doc.text(), partition, offset);
    }
}




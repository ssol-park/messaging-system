package com.kafka.study.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.jsoup.nodes.Document.OutputSettings.Syntax.html;

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

        String html = scrapService.getScrapData(postId).html();
        log.info("[rawScrapDataListen] instanceId:{}, Received message: {}, partition: {}, offset: {}", instanceId, html, partition, offset);

        kafkaProducer.processData(html);
    }

    @KafkaListener(topics = "${kafka.topics.processed-data}", groupId = "${kafka.consumer-groups.processed-data-group}")
    public void processedDataListen(String data,
                                   @Header(KafkaHeaders.RECEIVED_PARTITION)  int partition,
                                   @Header(KafkaHeaders.OFFSET)  int offset) {

        Document document = Jsoup.parse(data);

        log.info("[processedDataListen] instanceId:{}, partition: {}, offset: {} data:{}", instanceId, partition, offset, document);
        log.info("Body ::: {}", document.body());
        log.info("Body data ::: {}", document.body().text());

        // TODO :: 가공 로직
    }
}




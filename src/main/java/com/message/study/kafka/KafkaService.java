package com.kafka.study.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaService {

    private final KafkaProducer kafkaProducer;

    public KafkaService(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public void scrapData() {
        for (int i = 1; i <= 10; i++) {
            kafkaProducer.scrapData(String.valueOf(i));
        }
    }
}

package com.message.study.kafka;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
public class KafkaController {
    private final KafkaService kafkaService;

    public KafkaController(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @GetMapping("/scrap")
    public ResponseEntity<Void> scrapData() {
        kafkaService.scrapData();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

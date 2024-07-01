package com.kafka.study.example;

import org.springframework.web.bind.annotation.*;

@RestController
public class KafkaController {
    private final KafkaProducer kafkaProducer;

    public KafkaController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        kafkaProducer.sendMessage(message);
        return "Message sent to topic";
    }

    @GetMapping("/send/{partitionId}")
    public String sendMessage(@PathVariable(name = "partitionId") int partitionId, @RequestParam String message) {
        kafkaProducer.sendMessage(partitionId, message);
        return "Message sent to topic";
    }
}

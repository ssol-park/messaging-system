package com.rabbitmq.controller;

import com.rabbitmq.producer.DirectProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final DirectProducer directProducer;

    public MessageController(DirectProducer directProducer) {
        this.directProducer = directProducer;
    }

    @GetMapping("/direct")
    public String sendDirectMessage(@RequestParam(value = "message") String message) {
        directProducer.sendMessage(message);
        return message;
    }
}

package com.rabbitmq.controller;

import com.rabbitmq.producer.DirectProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final DirectProducer directProducer;
    private AtomicInteger counter = new AtomicInteger(0);

    public MessageController(DirectProducer directProducer) {
        this.directProducer = directProducer;
    }

    @GetMapping("/direct")
    public String sendDirectMessage(@RequestParam(value = "message") String message) {
        directProducer.sendMessage(message + counter.incrementAndGet());
        return message;
    }
}

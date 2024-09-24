package com.rabbitmq.controller;

import com.rabbitmq.producer.DirectProducer;
import com.rabbitmq.producer.TopicProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final TopicProducer topicProducer;
    private final DirectProducer directProducer;

    public MessageController(TopicProducer topicProducer, DirectProducer directProducer) {
        this.topicProducer = topicProducer;
        this.directProducer = directProducer;
    }

    @GetMapping("/topic")
    public String sendTopicMessage(@RequestParam(value = "logLevel", required = false) String logLevel, @RequestParam(value = "message") String message) {
        topicProducer.sendMessage("serviceA", logLevel, message);
        return message;
    }

    @GetMapping("/direct")
    public String sendDirectMessage(@RequestParam(value = "message") String message) {
        directProducer.sendMessage(message);
        return message;
    }
}

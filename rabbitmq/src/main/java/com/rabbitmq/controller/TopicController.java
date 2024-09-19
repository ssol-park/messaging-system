package com.rabbitmq.controller;

import com.rabbitmq.producer.TopicProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topic")
public class TopicController {

    private final TopicProducer topicProducer;

    public TopicController(TopicProducer topicProducer) {
        this.topicProducer = topicProducer;
    }

    @GetMapping
    public String sendTopicMessage(@RequestParam(value = "logLevel", required = false) String logLevel, @RequestParam(value = "message") String message) {
        topicProducer.sendMessage("serviceA", logLevel, message);
        return message;
    }
}

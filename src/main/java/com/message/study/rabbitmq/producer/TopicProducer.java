package com.message.study.rabbitmq.producer;

import com.message.study.rabbitmq.config.RabbitMQProperties;
import org.springframework.stereotype.Service;

@Service
public class TopicProducer {

    private final RabbitMQMessageSender messageSender;
    private final RabbitMQProperties properties;

    public TopicProducer(RabbitMQMessageSender messageSender, RabbitMQProperties properties) {
        this.messageSender = messageSender;
        this.properties = properties;
    }

}

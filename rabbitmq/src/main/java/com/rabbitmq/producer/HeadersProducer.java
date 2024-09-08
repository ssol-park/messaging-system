package com.rabbitmq.producer;

import com.rabbitmq.config.RabbitMQProperties;
import org.springframework.stereotype.Service;

@Service
public class HeadersProducer {

    private final RabbitMQMessageSender messageSender;
    private final RabbitMQProperties properties;

    public HeadersProducer(RabbitMQMessageSender messageSender, RabbitMQProperties properties) {
        this.messageSender = messageSender;
        this.properties = properties;
    }
}
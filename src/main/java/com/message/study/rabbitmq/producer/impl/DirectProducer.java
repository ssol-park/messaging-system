package com.message.study.rabbitmq.producer.impl;

import com.message.study.rabbitmq.config.RabbitMQProperties;
import com.message.study.rabbitmq.producer.RabbitMQMessageSender;
import com.message.study.rabbitmq.producer.RabbitMQProducer;
import org.springframework.stereotype.Service;

@Service
public class DirectProducer implements RabbitMQProducer {

    private final RabbitMQMessageSender messageSender;
    private final RabbitMQProperties properties;

    public DirectProducer(RabbitMQMessageSender messageSender, RabbitMQProperties properties) {
        this.messageSender = messageSender;
        this.properties = properties;
    }

    @Override
    public void sendMessage(String message) {
        messageSender.sendMessage(properties.getDirect().getExchange(), properties.getDirect().getRoutingKey(), message);
    }
}

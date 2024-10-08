package com.rabbitmq.producer;

import com.rabbitmq.config.RabbitMQMessageSender;
import com.rabbitmq.config.RabbitMQProperties;
import org.springframework.stereotype.Service;

@Service
public class DirectProducer {

    private final RabbitMQMessageSender messageSender;
    private final RabbitMQProperties properties;

    public DirectProducer(RabbitMQMessageSender messageSender, RabbitMQProperties properties) {
        this.messageSender = messageSender;
        this.properties = properties;
    }

    public void sendMessage(String message) {
        messageSender.sendMessage(properties.getDirect().getExchange(), properties.getDirect().getRoutingKey(), message);
    }
}

package com.rabbitmq.producer;

import com.rabbitmq.config.RabbitMQMessageSender;
import com.rabbitmq.config.RabbitMQProperties;
import org.springframework.stereotype.Service;

@Service
public class TopicProducer {

    private final RabbitMQMessageSender messageSender;
    private final RabbitMQProperties properties;

    public TopicProducer(RabbitMQMessageSender messageSender, RabbitMQProperties properties) {
        this.messageSender = messageSender;
        this.properties = properties;
    }

    public void sendMessage(String service, String logLevel, String message) {
        String routingKey = service + "." + logLevel + ".log";
        messageSender.sendMessage(properties.getTopic().getExchange(), routingKey, message);
    }
}

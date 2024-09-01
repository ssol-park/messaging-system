package com.message.study.rabbitmq.producer.impl;

import com.message.study.rabbitmq.config.RabbitMQProperties;
import com.message.study.rabbitmq.producer.RabbitMQMessageSender;
import com.message.study.rabbitmq.producer.RabbitMQProducer;
import org.springframework.stereotype.Service;

@Service
public class HeadersProducer implements RabbitMQProducer {

    private final RabbitMQMessageSender messageSender;
    private final RabbitMQProperties properties;

    public HeadersProducer(RabbitMQMessageSender messageSender, RabbitMQProperties properties) {
        this.messageSender = messageSender;
        this.properties = properties;
    }

    @Override
    public void sendMessage(String message) {
        messageSender.sendMessage(properties.getHeaders().getExchange(), "", message, m -> {
            m.getMessageProperties().setHeader("key1", "value1");
            m.getMessageProperties().setHeader("key2", "value2");
            return m;
        });
    }
}

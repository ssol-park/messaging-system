package com.rabbitmq.config;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQMessageSender {
    private final RabbitTemplate template;

    public RabbitMQMessageSender(RabbitTemplate template) {
        this.template = template;
    }

    public void sendMessage(String exchange, String routingKey, String message) {
        template.convertAndSend(exchange, routingKey, message);
    }
}

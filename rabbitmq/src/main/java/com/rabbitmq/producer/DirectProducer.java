package com.rabbitmq.producer;

import com.rabbitmq.config.RabbitMQMessageSender;
import com.rabbitmq.config.RabbitMQProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DirectProducer {

    private static final Logger logger = LoggerFactory.getLogger(DirectProducer.class);
    private final RabbitMQMessageSender messageSender;
    private final RabbitMQProperties properties;

    public DirectProducer(RabbitMQMessageSender messageSender, RabbitMQProperties properties) {
        this.messageSender = messageSender;
        this.properties = properties;
    }

    public void sendMessage(String message) {
        messageSender.sendMessage(properties.getDirect().getExchange(), properties.getDirect().getRoutingKey(), message);
    }

    public void sendDelayMessage(String message) {
        logger.info("[sendDelayMessage] Delay Message: {}", message);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        logger.info("[sendDelayMessage] Send Message: {}", message);
        sendMessage(message);
    }
}

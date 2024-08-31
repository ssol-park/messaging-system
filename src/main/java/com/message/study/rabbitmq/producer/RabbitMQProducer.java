package com.message.study.rabbitmq.producer;

public interface RabbitMQProducer {
    void sendMessage(String message);
}

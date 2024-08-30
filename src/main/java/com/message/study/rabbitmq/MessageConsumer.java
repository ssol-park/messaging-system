package com.message.study.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageConsumer {

    @RabbitListener(queues = "myQueue")
    public void receiveMessage(String message) {
        log.info("Received message: {}", message);
    }
}

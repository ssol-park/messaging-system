package com.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class DirectConsumer {
    @RabbitListener(queues =  "direct-queue")
    public void receiveMessage(String message) throws TimeoutException {
        log.info("[DirectConsumer] receiveMessage {} ", message);
    }
}

package com.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMQConsumer {

    @RabbitListener(queues = "direct_queue")
    public void receiveDirectMessage(String message) {
        log.info("Received Direct Message {}", message);
    }

    @RabbitListener(queues = "fanout_queue_1")
    public void receiveFanoutMessage1(String message) {
        log.info("Received Fanout Message (Queue 1) {}", message);
    }

    @RabbitListener(queues = "fanout_queue_2")
    public void receiveFanoutMessage2(String message) {
        log.info("Received Fanout Message (Queue 2) {}", message);
    }

    @RabbitListener(queues = "headers_queue")
    public void receiveHeadersMessage(String message) {
        log.info("Received Headers Message {}", message);
    }
}

package com.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TopicConsumer {
    @RabbitListener(queues =  "error_queue")
    public void receiveErrorLog(String message) {
        log.error("[TopicConsumer] {}", message);
    }

    @RabbitListener(queues = "all_logs_queue")
    public void receiveAllLogs(String message) {
        log.info("[TopicConsumer] {}", message);
    }
}

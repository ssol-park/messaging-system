package com.rabbitmq.consumer;

import com.rabbitmq.config.RabbitMQProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TopicConsumer {

    @RabbitListener(queues =  "error-queue", id = "receiveErrorLogsId")
    public String receiveErrorLogs(String message) {
        log.error("[TopicConsumer] ERROR :: {}", message);
        return message;
    }

    @RabbitListener(queues = "all-logs-queue")
    public String receiveAllLogs(String message) {
        log.info("[TopicConsumer] INFO :: {}", message);
        return message;
    }
}

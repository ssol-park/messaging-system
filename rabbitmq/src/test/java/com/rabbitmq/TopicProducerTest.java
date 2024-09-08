package com.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.consumer.TopicConsumer;
import com.rabbitmq.producer.TopicProducer;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.Mockito.verify;

@SpringBootTest
@RabbitListenerTest
class TopicProducerTest {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TopicProducer topicProducer;

    @SpyBean
    private TopicConsumer topicConsumer;

    @Test
    void testSendMessageToErrorQueue() throws JsonProcessingException {
        // given
        String service = "serviceA";
        String logLevel = "error";
        String message = "error log";

        // when
        topicProducer.sendMessage(service, logLevel, message);

        // then
        // 리스너가 제대로 메시지를 리슨했는지 확인, Jackson2JsonMessageConverter 로 인해 메세지가 직렬화 되므로, 동일하게 직렬화 후 확인
        verify(topicConsumer).receiveErrorLogs(objectMapper.writeValueAsString(message));
    }

    @Test
    void testSendMessageToAllLogsQueue() throws JsonProcessingException {
        // given
        String service = "serviceA";
        String logLevel = "info";
        String message = "infolog";

        // when
        topicProducer.sendMessage(service, logLevel, message);

        // then
        // 리스너가 제대로 메시지를 리슨했는지 확인
        verify(topicConsumer).receiveAllLogs(objectMapper.writeValueAsString(message));
    }
}

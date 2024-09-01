package com.message.study.rabbitmq;

import com.message.study.rabbitmq.config.RabbitMQProperties;
import com.message.study.rabbitmq.producer.RabbitMQMessageSender;
import com.message.study.rabbitmq.producer.impl.TopicProducer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
@SpringBootTest
class RabbitMQProducerTest {

    @Autowired
    private RabbitMQMessageSender messageSender;

    @Autowired
    private TopicProducer topicProducer;
    @Autowired
    private RabbitMQProperties properties;

    @Test
    void testTopicSendMessage() {
        // given
        String message = "send topic";

        // when
        topicProducer.sendMessage(message);

        // then
        verify(messageSender, times(1)).sendMessage(
                properties.getTopic().getExchange(),
                properties.getTopic().getRoutingKey(),
                message
        );

        assertThat(message).isNotEmpty()
                           .isEqualTo("send topic");
    }
}

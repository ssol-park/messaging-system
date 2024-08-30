package com.message.study.rabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class MessageProducerTest {

    @Autowired
    private MessageProducer messageProducer;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    void testSendMessage() {
        // given
        String message = "send message";

        // when
        messageProducer.sendMessage(message);

        // then
        verify(rabbitTemplate, times(1)).convertAndSend("myQueue", message);

        assertThat(message).isNotEmpty()
                           .isEqualTo("send message");
    }
}

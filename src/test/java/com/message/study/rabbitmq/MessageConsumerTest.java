package com.message.study.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@Slf4j
@SpringBootTest
class MessageConsumerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @SpyBean
    private MessageConsumer messageConsumer;

    @Test
    void testReceiveMessage() throws InterruptedException {
        // given
        String message = "send message";

        // when: 메세지 전송
        rabbitTemplate.convertAndSend("myQueue", message);

        Thread.sleep(1000);

        // then: 메시지 수신 여부 확인
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(messageConsumer).receiveMessage(messageCaptor.capture());
//        log.info("messageCaptor :: {}", messageCaptor.getValue());

        // AssertJ를 사용하여 메시지 검증
        assertThat(messageCaptor.getValue()).isNotNull()
                .isEqualTo(message);
    }
}

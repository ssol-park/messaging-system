package com.message.study.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /*
    * 내구성(durable)
    *  ture: 서버재시작 및 충돌 시 에도 큐가 삭제되지 않으며 저장된 메세지도 유지됨
    *  false: 서버재시작 시 큐와, 큐에 저장된 메시지가 사라짐
    * */
    @Bean
    public Queue myQueue() {
        return new Queue("myQueue", false);
    }

}

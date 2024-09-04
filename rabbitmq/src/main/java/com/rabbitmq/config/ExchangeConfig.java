package com.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeConfig {

    private final RabbitMQProperties rabbitMQProperties;

    public ExchangeConfig(RabbitMQProperties rabbitMQProperties) {
        this.rabbitMQProperties = rabbitMQProperties;
    }

    /*
    * 내구성(durable)
    *  ture: 서버재시작 및 충돌 시 에도 큐가 삭제되지 않으며 저장된 메세지도 유지됨
    *  false: 서버재시작 시 큐와, 큐에 저장된 메시지가 사라짐
    * */
    // Direct Exchange 설정
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(rabbitMQProperties.getDirect().getExchange());
    }

    @Bean
    public Queue directQueue() {
        return new Queue(rabbitMQProperties.getDirect().getQueue(), true);
    }

    @Bean
    public Binding directBinding(DirectExchange directExchange, Queue directQueue) {
        return BindingBuilder.bind(directQueue).to(directExchange).with(rabbitMQProperties.getDirect().getRoutingKey());
    }

    // Topic Exchange 설정
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(rabbitMQProperties.getTopic().getExchange());
    }

    @Bean
    public Queue errorQueue() {
        return new Queue(rabbitMQProperties.getTopic().getErrorQueue(), true);
    }

    @Bean
    public Queue allLogsQueue() {
        return new Queue(rabbitMQProperties.getTopic().getAllLogsQueue(), true);
    }

    @Bean
    public Binding errorBinding(TopicExchange topicExchange, Queue errorQueue) {
        return BindingBuilder.bind(errorQueue).to(topicExchange).with(rabbitMQProperties.getTopic().getErrorRoutingKey());
    }

    @Bean
    public Binding allLogsBinding(TopicExchange topicExchange, Queue allLogsQueue) {
        return BindingBuilder.bind(allLogsQueue).to(topicExchange).with(rabbitMQProperties.getTopic().getAllLogsRoutingKey());
    }

}

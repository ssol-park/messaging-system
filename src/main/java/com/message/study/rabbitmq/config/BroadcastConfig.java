package com.message.study.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BroadcastConfig {

    private final RabbitMQProperties rabbitMQProperties;

    public BroadcastConfig(RabbitMQProperties rabbitMQProperties) {
        this.rabbitMQProperties = rabbitMQProperties;
    }

    // Fanout Exchange 설정
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(rabbitMQProperties.getFanout().getExchange());
    }

    @Bean
    public Queue fanoutQueue1() {
        return new Queue(rabbitMQProperties.getFanout().getQueue1(), true);
    }

    @Bean
    public Queue fanoutQueue2() {
        return new Queue(rabbitMQProperties.getFanout().getQueue2(), true);
    }

    @Bean
    public Binding fanoutBinding1(FanoutExchange fanoutExchange, Queue fanoutQueue1) {
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    @Bean
    public Binding fanoutBinding2(FanoutExchange fanoutExchange, Queue fanoutQueue2) {
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }

    // Headers Exchange 설정
    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(rabbitMQProperties.getHeaders().getExchange());
    }

    @Bean
    public Queue headersQueue() {
        return new Queue(rabbitMQProperties.getHeaders().getQueue(), true);
    }

    @Bean
    public Binding headersBinding(HeadersExchange headersExchange, Queue headersQueue) {
        return BindingBuilder.bind(headersQueue).to(headersExchange).whereAll("key1", "key2").exist();
    }
}

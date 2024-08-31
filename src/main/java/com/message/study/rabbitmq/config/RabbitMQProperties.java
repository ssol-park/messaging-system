package com.message.study.rabbitmq.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMQProperties {

    private final Direct direct;
    private final Topic topic;
    private final Fanout fanout;
    private final Headers headers;

    public RabbitMQProperties(Direct direct, Topic topic, Fanout fanout, Headers headers) {
        this.direct = direct;
        this.topic = topic;
        this.fanout = fanout;
        this.headers = headers;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Direct {
        private final String exchange;
        private final String queue;
        private final String routingKey;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Topic {
        private final String exchange;
        private final String queue;
        private final String routingKey;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Fanout {
        private final String exchange;
        private final String queue1;
        private final String queue2;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Headers {
        private final String exchange;
        private final String queue;
    }
}

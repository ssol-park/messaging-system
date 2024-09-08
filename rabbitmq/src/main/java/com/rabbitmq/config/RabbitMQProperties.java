package com.rabbitmq.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMQProperties {

    private final String host;
    private final int port;
    private final String username;
    private final String password;

    private final Direct direct;
    private final Topic topic;
    private final Fanout fanout;
    private final Headers headers;

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
        private final String errorQueue;
        private final String allLogsQueue;
        private final String errorRoutingKey;
        private final String allLogsRoutingKey;
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

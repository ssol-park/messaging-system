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

    @Getter
    @RequiredArgsConstructor
    public static class Direct {
        private final String exchange;
        private final String queue;
        private final String routingKey;
    }
}

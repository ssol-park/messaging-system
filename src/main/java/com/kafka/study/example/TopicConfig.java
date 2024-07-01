package com.kafka.study.example;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {

    @Value("${kafka.topic.replication-factor}")
    private short replicationFactor;

    @Value("${kafka.topic.name}")
    private String topic;

    @Bean
    public NewTopic psrTopic() {
        return new NewTopic(topic, 1, replicationFactor);
    }
}

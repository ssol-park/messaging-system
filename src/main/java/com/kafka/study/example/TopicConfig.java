package com.kafka.study.example;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {

    @Value("${kafka.replication-factor}")
    private short replicationFactor;

    @Value("${kafka.psr-topic.name}")
    private String psrTopic;

    @Value("${kafka.raw-scrap-data.name}")
    private String rawScrapData;

    @Bean
    public NewTopic psrTopic() {
        return new NewTopic(psrTopic, 2, replicationFactor);
    }

    @Bean
    public NewTopic rawScrapDataTopic() {
        return new NewTopic(rawScrapData, 2, replicationFactor);
    }
}

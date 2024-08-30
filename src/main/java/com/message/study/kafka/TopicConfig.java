package com.message.study.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {

    @Value("${kafka.replication-factor}")
    private short replicationFactor;

    @Value("${kafka.topics.raw-scrap-data}")
    private String rawScrapData;

    @Bean
    public NewTopic rawScrapDataTopic() {
        return new NewTopic(rawScrapData, 2, replicationFactor);
    }
}

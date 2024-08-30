package com.kafka.study.example;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.consumer-groups.raw-scrap-group}")
    private String scrapGroupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.listener.concurrency}")
    private int concurrency;

    private Map<String, Object> commonConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    private Map<String, Object> consumerConfig(String groupId) {
        Map<String, Object> props = commonConfigs();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        return props;
    }

    private ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(String groupId) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfig(groupId)));
        factory.setConcurrency(concurrency);

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> scrapKafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory(scrapGroupId);
    }

    /*
    @Value("${kafka.consumer.data.group-id}")
    private String dataGroupId;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> dataKafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory(dataGroupId);
    }

    @Value("${kafka.consumer.db.group-id}")
    private String dbGroupId;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> dbKafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory(dbGroupId);
    }*/
}

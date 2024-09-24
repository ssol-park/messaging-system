package com.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.net.SocketTimeoutException;
import java.rmi.ConnectIOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class RabbitMQConfig {

    private final RabbitMQProperties rabbitMQProperties;
    private final CustomErrorHandler customErrorHandler;

    public RabbitMQConfig(RabbitMQProperties rabbitMQProperties, CustomErrorHandler customErrorHandler) {
        this.rabbitMQProperties = rabbitMQProperties;
        this.customErrorHandler = customErrorHandler;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitMQProperties.getHost());
        connectionFactory.setPort(rabbitMQProperties.getPort());
        connectionFactory.setUsername(rabbitMQProperties.getUsername());
        connectionFactory.setPassword(rabbitMQProperties.getPassword());

        /*
        // TLS/SSL 설정
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            connectionFactory.getRabbitConnectionFactory().useSslProtocol(sslContext);
            connectionFactory.setPort(5671);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        return connectionFactory;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(20);  // 기본 스레드 수
        taskExecutor.setMaxPoolSize(20);   // 최대 스레드 수
        taskExecutor.setQueueCapacity(100);  // 대기 큐 용량
        taskExecutor.setThreadNamePrefix("RabbitMQ-Executor-");  // 스레드 이름 접두사
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        // 1. ConnectionFactory 설정
        factory.setConnectionFactory(connectionFactory);

        // 2. 동시 처리 스레드 수 (기본값: 1) // 기본적으로 3개의 스레드가 동시에 메시지를 처리
        factory.setConcurrentConsumers(3);

        // 3. 최대 동시 처리 스레드 수 (기본값: 제한 없음) // 필요 시 최대 10개의 스레드로 동적 확장
        factory.setMaxConcurrentConsumers(10);

        // 4. Prefetch Count (기본값: 1) // 각 스레드는 한 번에 5개의 메시지를 가져와 처리
        factory.setPrefetchCount(3);

        // 5. ACK 모드 (기본값: AcknowledgeMode.AUTO) // 자동 ACK, 처리 후 RabbitMQ에 자동으로 메시지 승인
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);

        // 6. 트랜잭션 활성화 (기본값: false) // 트랜잭션을 사용하지 않음, 필요한 경우 true로 설정
        factory.setChannelTransacted(false);

        // 7. TaskExecutor 설정 (기본 스레드 풀 사용) // 기본 스레드 풀 사용, 필요시 커스텀 Executor 사용 가능
        factory.setTaskExecutor(taskExecutor());

        // 8. 메시지 처리 타임아웃 설정 (기본값: 없음) // 메시지를 기다리는 최대 시간 (3초), 이후 스레드 대기 해제
        factory.setReceiveTimeout(3000L);

        // 9. 처리 실패 시 메시지 재큐 설정 (기본값: true)
        factory.setDefaultRequeueRejected(true); // 메시지 처리 실패 시 큐로 다시 넣음

        // 10. 연결 재시도 간격 설정 (기본값: 5000ms)
        factory.setRecoveryInterval(5000L); // 연결이 끊어졌을 때 5초마다 재시도

        // 11. 메시지 처리 후 유휴 상태 시 이벤트 발생 간격 설정 (기본값: 60000ms)
        factory.setIdleEventInterval(10000L); // 메시지가 없는 유휴 상태에서 이벤트가 발생하는 간격 (60초)

        factory.setRetryTemplate(retryTemplate());
        factory.setErrorHandler(customErrorHandler);

        // 13. Consumer 태그 설정 (기본값: 자동 생성)
//        factory.setConsumerTagStrategy(queue -> "MyConsumerTag_" + queue); // 각 소비자에 대해 고유한 태그 부여


        return factory;
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(exceptionClassifierRetryPolicy());
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy());
        return retryTemplate;
    }

    @Bean
    public FixedBackOffPolicy fixedBackOffPolicy() {
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(3000);
        return fixedBackOffPolicy;
    }

    @Bean
    public ExceptionClassifierRetryPolicy exceptionClassifierRetryPolicy() {
        ExceptionClassifierRetryPolicy retryPolicy = new ExceptionClassifierRetryPolicy();

        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
        retryableExceptions.put(ConnectIOException.class, true);  // 이 예외는 재시도
        retryableExceptions.put(SocketTimeoutException.class, true);  // 이 예외도 재시도
        retryableExceptions.put(ListenerExecutionFailedException.class, true);

        SimpleRetryPolicy policy = new SimpleRetryPolicy(5, retryableExceptions);

        Map<Class<? extends Throwable>, RetryPolicy> policyMap = new HashMap<>();
        policyMap.put(ConnectIOException.class, policy);
        policyMap.put(SocketTimeoutException.class, policy);
        policyMap.put(ListenerExecutionFailedException.class, policy);

        retryPolicy.setPolicyMap(policyMap);
        return retryPolicy;
    }
}

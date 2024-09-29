package com.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;

@Slf4j
@Configuration
public class RabbitMQConfig {

    public static final String DLX_EXCHANGE = "dlx-exchange";
    public static final String DLX_QUEUE = "dlx-queue";
    public static final String DLX_ROUTING_KEY = "dlx-routing-key";

    private final RabbitMQProperties rabbitMQProperties;
    private final CustomErrorHandler customErrorHandler;

    public RabbitMQConfig(RabbitMQProperties rabbitMQProperties, CustomErrorHandler customErrorHandler) {
        this.rabbitMQProperties = rabbitMQProperties;
        this.customErrorHandler = customErrorHandler;
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
        return QueueBuilder.durable(rabbitMQProperties.getDirect().getQueue())
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLX_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding directBinding(DirectExchange directExchange, Queue directQueue) {
        return BindingBuilder.bind(directQueue).to(directExchange).with(rabbitMQProperties.getDirect().getRoutingKey());
    }

    // DLX

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }
    @Bean
    public Queue dlxQueue() {
        return new Queue(DLX_QUEUE, true);
    }

    @Bean
    public Binding bindingDlxQueue(DirectExchange dlxExchange, Queue dlxQueue) {
        return BindingBuilder.bind(dlxQueue).to(dlxExchange).with(DLX_ROUTING_KEY);
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
        factory.setPrefetchCount(5);

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

        factory.setErrorHandler(customErrorHandler);

        // 13. Consumer 태그 설정 (기본값: 자동 생성)
//        factory.setConsumerTagStrategy(queue -> "MyConsumerTag_" + queue); // 각 소비자에 대해 고유한 태그 부여


        factory.setAdviceChain(
                RetryInterceptorBuilder.stateless()
                        .maxAttempts(3) // 최초 시도 1회 + 재시도 2회
                        .backOffOptions(Duration.ofSeconds(3L).toMillis(), 2, Duration.ofSeconds(10L).toMillis()) // Exponential Backoff
                        .recoverer(new RejectAndDontRequeueRecoverer()) // 재시도가 모두 실패 시 해당 예외를 발생시켜 DLX 로 전달
                        .build()
        );


        return factory;
    }
}

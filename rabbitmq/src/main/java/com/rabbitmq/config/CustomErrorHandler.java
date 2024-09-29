package com.rabbitmq.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@ConfigurationProperties(prefix = "retry")
public class CustomErrorHandler implements ErrorHandler {

    private List<String> exceptions;
    private List<Class<? extends Throwable>> retryExceptions = new ArrayList<>();

    @PostConstruct
    public void init() {
        for (String exception : exceptions) {
            try {
                Class<? extends Throwable> exceptionClass = (Class<? extends Throwable>) Class.forName(exception);
                retryExceptions.add(exceptionClass);

            } catch (ClassNotFoundException e) {
                log.error("[CustomErrorHandler] 예외 클래스 {}를 찾을 수 없습니다.", exception, e);
            }
        }
    }

    @Override
    public void handleError(Throwable t) {
        log.error("[CustomErrorHandler] {}", t.getMessage());

        if(isRetryableException(t)) {
            log.info("[CustomErrorHandler] 재시도 가능한 예외 발생: {}", t.getCause());

        }else {
            throw new AmqpRejectAndDontRequeueException(t);
        }

    }

    // 재시도 가능한 예외인지 여부를 확인하는 메서드
    private boolean isRetryableException(Throwable cause) {

        if(cause == null) {
            return false;
        }

        return retryExceptions.stream().anyMatch(exception -> exception.isAssignableFrom(cause.getClass()));
    }

    public void setExceptions(List<String> exceptions) {
        this.exceptions = exceptions;
    }
}

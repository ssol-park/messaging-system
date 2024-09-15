package com.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Slf4j
@Component
public class CustomErrorHandler implements ErrorHandler {
    @Override
    public void handleError(Throwable t) {
        log.info("ERROR :: {}", t.getMessage());
        t.printStackTrace();

        // 상태 처리
    }
}

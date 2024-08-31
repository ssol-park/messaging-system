package com.message.study;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Slf4j
@SpringBootApplication
@ConfigurationPropertiesScan
public class StudyApplication implements CommandLineRunner {

    @Value("${server.port}")
    private String serverPort;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public static void main(String[] args) {
        SpringApplication.run(StudyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Active Profile: {}", activeProfile);
        log.info("Server Port: {}", serverPort);
    }

}

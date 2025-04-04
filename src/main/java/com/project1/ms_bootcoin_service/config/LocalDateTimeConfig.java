package com.project1.ms_bootcoin_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class LocalDateTimeConfig {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}

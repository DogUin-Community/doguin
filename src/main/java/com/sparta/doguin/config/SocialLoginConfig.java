package com.sparta.doguin.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class SocialLoginConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                // 서버의 응답대기시간 최대 5분 설정
                .setReadTimeout(Duration.ofMinutes(5))
                // 서버에 연결 시도 시간 최대 5분 설정
                .setConnectTimeout(Duration.ofMinutes(5))
                .build();
    }
}
package com.sparta.doguin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 동시에 실행 될 수 있는 기본 쓰레드 갯수
        executor.setCorePoolSize(5);
        // 쓰레드가 추가로 필요할때 생성할 수 있는 최대 쓰레드 갯수
        executor.setMaxPoolSize(10);
        // 작업 큐의 공간 설정 (기본 쓰레드가 사용중일때 최대 500개의 작업을 대기 상태로 둘 수 있음)
        executor.setQueueCapacity(500);
        // 생성된 쓰레드의 접두사를 지정
        executor.setThreadNamePrefix("S3-Async-");
        // 쓰레드 풀 초기화
        executor.initialize();
        return executor;
    }
}

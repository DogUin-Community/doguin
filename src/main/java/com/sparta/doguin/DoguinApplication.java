package com.sparta.doguin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@EnableJpaAuditing
@EnableCaching // EnCache사용을 위한 어노테이션
@SpringBootApplication
@EnableScheduling // @scheduled를 위함
@EnableAsync // 비동기를 위함
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class DoguinApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoguinApplication.class, args);
    }


}

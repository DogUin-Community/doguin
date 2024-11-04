package com.sparta.doguin.config;

import com.sparta.doguin.aop.AspectModule;
import com.sparta.doguin.notification.discord.DiscordMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AspectConfig {
    private final DiscordMessageService dms;
    /**
     * AOP 모듈 등록
     */
    @Bean
    public AspectModule aspectModule() {
        return new AspectModule(dms);
    }
}

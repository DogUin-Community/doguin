package com.sparta.doguin.domain.user.service.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.doguin.domain.common.exception.UserException;
import com.sparta.doguin.domain.common.response.ApiResponseUserEnum;
import com.sparta.doguin.domain.user.enums.SocialProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class SocialLoginStrategyFactory {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${social.kakao.client-id}")
    private String kakaoClientId;

    @Value("${social.naver.client-id}")
    private String naverClientId;

    @Value("${social.naver.client-secret}")
    private String naverClientSecret;

    @Value("${social.google.client-id}")
    private String googleClientId;

    @Value("${social.google.client-secret}")
    private String googleClientSecret;

    @Value("${social.github.client-id}")
    private String githubClientId;

    @Value("${social.github.client-secret}")
    private String githubClientSecret;

    public SocialLoginStrategy getStrategy(SocialProvider provider) {
        switch (provider) {
            case KAKAO:
                return new KakaoStrategy(restTemplate, kakaoClientId, objectMapper);
            case NAVER:
                return new NaverStrategy(restTemplate, naverClientId, naverClientSecret, objectMapper);
            case GOOGLE:
                return new GoogleStrategy(restTemplate, googleClientId, googleClientSecret, objectMapper);
            case GITHUB:
                return new GitHubStrategy(restTemplate, githubClientId, githubClientSecret, objectMapper);
            default:
                throw new UserException(ApiResponseUserEnum.INVALID_SOCIAL_PROVIDER);
        }
    }
}
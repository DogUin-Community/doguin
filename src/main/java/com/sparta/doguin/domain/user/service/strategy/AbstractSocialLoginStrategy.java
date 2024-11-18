package com.sparta.doguin.domain.user.service.strategy;

import com.sparta.doguin.domain.common.exception.UserException;
import com.sparta.doguin.domain.common.response.ApiResponseUserEnum;
import com.sparta.doguin.domain.user.enums.SocialProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public abstract class AbstractSocialLoginStrategy implements SocialLoginStrategy {
    protected final RestTemplate restTemplate;
    protected final String clientId;
    protected final String clientSecret;

    protected AbstractSocialLoginStrategy(RestTemplate restTemplate, String clientId) {
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.clientSecret = null;
    }

    protected AbstractSocialLoginStrategy(RestTemplate restTemplate, String clientId, String clientSecret) {
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    protected String postForAccessToken(SocialProvider provider, MultiValueMap<String, String> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // client_secret이 존재하는 경우에만 추가
        if (clientSecret != null && !clientSecret.isEmpty()) {
            params.add("client_secret", clientSecret);
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        Map<String, Object> response = restTemplate.postForObject(provider.getTokenUrl(), request, Map.class);

        if (response == null || !response.containsKey("access_token")) {
            throw new UserException(ApiResponseUserEnum.FAILED_TO_FETCH_SOCIAL_ACCESS_TOKEN);
        }

        return (String) response.get("access_token");
    }
}
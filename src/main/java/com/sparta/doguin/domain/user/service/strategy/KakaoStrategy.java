package com.sparta.doguin.domain.user.service.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.doguin.domain.common.exception.UserException;
import com.sparta.doguin.domain.common.response.ApiResponseUserEnum;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.enums.SocialProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class KakaoStrategy extends AbstractSocialLoginStrategy {
    private final ObjectMapper objectMapper;

    public KakaoStrategy(RestTemplate restTemplate, String clientId, ObjectMapper objectMapper) {
        super(restTemplate, clientId);
        this.objectMapper = objectMapper;
    }

    @Override
    public String getAccessToken(String code, String redirectUri) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        return postForAccessToken(SocialProvider.KAKAO, params);
    }

    @Override
    public User fetchUserInfo(String accessToken) throws JsonProcessingException {
        String url = SocialProvider.KAKAO.getUserInfoUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        String responseBody = restTemplate.exchange(url, HttpMethod.GET, request, String.class).getBody();

        if (responseBody == null) {
            throw new UserException(ApiResponseUserEnum.FAILED_TO_FETCH_SOCIAL_USER_INFO);
        }

        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();

        return User.builder()
                .email(email)
                .nickname(nickname)
                .build();
    }
}
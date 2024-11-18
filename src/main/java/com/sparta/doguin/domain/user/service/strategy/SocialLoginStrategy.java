package com.sparta.doguin.domain.user.service.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.doguin.domain.user.entity.User;

public interface SocialLoginStrategy {
    String getAccessToken(String code, String redirectUri);
    User fetchUserInfo(String accessToken) throws JsonProcessingException;
}
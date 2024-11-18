package com.sparta.doguin.domain.user.enums;

public enum SocialProvider {
    KAKAO("kakao", "https://kauth.kakao.com/oauth/token", "https://kapi.kakao.com/v2/user/me"),
    NAVER("naver", "https://nid.naver.com/oauth2.0/token", "https://openapi.naver.com/v1/nid/me"),
    GOOGLE("google", "https://oauth2.googleapis.com/token", "https://www.googleapis.com/oauth2/v3/userinfo"),
    GITHUB("github", "https://github.com/login/oauth/access_token", "https://api.github.com/user");

    private final String providerName;
    private final String tokenUrl;
    private final String userInfoUrl;

    SocialProvider(String providerName, String tokenUrl, String userInfoUrl) {
        this.providerName = providerName;
        this.tokenUrl = tokenUrl;
        this.userInfoUrl = userInfoUrl;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }
}
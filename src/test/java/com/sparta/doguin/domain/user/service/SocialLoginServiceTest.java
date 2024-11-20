package com.sparta.doguin.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.doguin.domain.common.exception.UserException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseUserEnum;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.enums.SocialProvider;
import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;
import com.sparta.doguin.domain.user.repository.UserRepository;
import com.sparta.doguin.domain.user.service.strategy.SocialLoginStrategy;
import com.sparta.doguin.domain.user.service.strategy.SocialLoginStrategyFactory;
import com.sparta.doguin.security.JwtUtil;
import com.sparta.doguin.security.dto.JwtUtilRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SocialLoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private SocialLoginStrategyFactory strategyFactory;

    @Mock
    private SocialLoginStrategy strategy;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private SocialLoginService socialLoginService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .email("test@example.com")
                .nickname("TestUser")
                .userType(UserType.INDIVIDUAL)
                .userRole(UserRole.ROLE_USER)
                .build();
    }

    @Test
    @DisplayName("소셜 로그인 성공 - 기존 유저")
    void socialLogin_existingUser() throws JsonProcessingException {
        // given
        String provider = "kakao";
        String code = "test-code";
        String accessToken = "test-access-token";
        String jwtToken = "test-jwt-token";

        given(strategyFactory.getStrategy(SocialProvider.KAKAO)).willReturn(strategy);
        given(strategy.getAccessToken(eq(code), anyString())).willReturn(accessToken); // 수정된 부분
        given(strategy.fetchUserInfo(accessToken)).willReturn(mockUser);
        given(userRepository.findByEmail(mockUser.getEmail())).willReturn(Optional.of(mockUser));
        given(jwtUtil.createToken(any(JwtUtilRequest.CreateToken.class))).willReturn(jwtToken);

        // when
        ApiResponse<String> result = socialLoginService.socialLogin(provider, code, response);

        // then
        assertEquals(ApiResponseUserEnum.USER_SOCIALOGIN_SUCCESS.getCode(), result.getCode());
        assertEquals(jwtToken, result.getData());
        verify(jwtUtil).addTokenToResponseHeader(jwtToken, response);
    }

    @Test
    @DisplayName("소셜 로그인 성공 - 신규 유저 등록")
    void socialLogin_newUser() throws JsonProcessingException {
        // given
        String provider = "google";
        String code = "test-code";
        String accessToken = "test-access-token";
        String jwtToken = "new-jwt-token";

        given(strategyFactory.getStrategy(SocialProvider.GOOGLE)).willReturn(strategy);
        given(strategy.getAccessToken(eq(code), anyString())).willReturn(accessToken); // 수정된 부분
        given(strategy.fetchUserInfo(accessToken)).willReturn(mockUser);
        given(userRepository.findByEmail(mockUser.getEmail())).willReturn(Optional.empty());
        given(jwtUtil.createToken(any(JwtUtilRequest.CreateToken.class))).willReturn(jwtToken);

        // when
        ApiResponse<String> result = socialLoginService.socialLogin(provider, code, response);

        // then
        assertEquals(ApiResponseUserEnum.USER_SOCIALOGIN_SUCCESS.getCode(), result.getCode());
        assertEquals(jwtToken, result.getData());
        verify(userRepository).save(any(User.class)); // 신규 유저 저장 확인
        verify(jwtUtil).addTokenToResponseHeader(jwtToken, response);
    }


    @Test
    @DisplayName("소셜 로그인 실패 - 제공하지 않는 소셜 로그인 방식")
    void socialLogin_invalidProvider() {
        // given
        String provider = "invalid";
        String code = "test-code";

        // when & then
        UserException exception = assertThrows(UserException.class, () ->
                socialLoginService.socialLogin(provider, code, response));
        assertEquals(ApiResponseUserEnum.INVALID_SOCIAL_PROVIDER.getMessage(), exception.getApiResponseEnum().getMessage());
    }

    @Test
    @DisplayName("사용자 정보 가져오기 실패")
    void fetchUserInfo_fail() throws JsonProcessingException {
        // given
        String provider = "github";
        String code = "test-code";
        String accessToken = "test-access-token";

        given(strategyFactory.getStrategy(SocialProvider.GITHUB)).willReturn(strategy);
        given(strategy.getAccessToken(eq(code), anyString())).willReturn(accessToken); // 수정된 부분
        given(strategy.fetchUserInfo(accessToken)).willThrow(new UserException(ApiResponseUserEnum.FAILED_TO_FETCH_SOCIAL_USER_INFO));

        // when & then
        UserException exception = assertThrows(UserException.class, () ->
                socialLoginService.socialLogin(provider, code, response));
        assertEquals(ApiResponseUserEnum.FAILED_TO_FETCH_SOCIAL_USER_INFO.getMessage(), exception.getApiResponseEnum().getMessage());
    }
}
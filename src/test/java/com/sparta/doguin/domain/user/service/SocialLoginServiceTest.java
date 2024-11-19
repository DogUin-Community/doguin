package com.sparta.doguin.domain.user.service;

import com.sparta.doguin.domain.common.exception.UserException;
import com.sparta.doguin.domain.user.repository.UserRepository;
import com.sparta.doguin.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class SocialLoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SocialLoginService socialLoginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    @DisplayName("기존 계정이 있는 사용자가 소셜 로그인으로 로그인 시도 시 비밀번호 필요 응답 반환")
//    void socialLogin_ExistingUserEmail_PasswordRequiredResponse() throws JsonProcessingException {
//        // given
//        String provider = "naver";
//        String code = "test-code";
//        HttpServletResponse response = mock(HttpServletResponse.class);
//        User existingUser = DataUtil.user1();
//
//        // Access Token 요청에 대한 RestTemplate 응답 Mock 설정
//        String accessToken = "naver-access-token";
//        when(restTemplate.postForObject(any(String.class), any(), eq(Map.class)))
//                .thenReturn(Map.of("access_token", accessToken));
//
//        // UserRepository가 기존 사용자 반환
//        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));
//
//        // 네이버 사용자 정보 응답을 기존 사용자와 동일한 이메일로 Mock 설정
//        String naverUserInfoResponse = "{ \"response\": { \"email\": \"" + existingUser.getEmail() + "\", \"nickname\": \"" + existingUser.getNickname() + "\" } }";
//        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(), eq(String.class)))
//                .thenReturn(new ResponseEntity<>(naverUserInfoResponse, HttpStatus.OK));
//
//        // when
//        ApiResponse<String> responseResult = socialLoginService.socialLogin(provider, code, response);
//
//        // then
//        assertEquals("비밀번호가 필요합니다. 기존 계정과 연동하려면 비밀번호를 입력하세요.", responseResult.getMessage());
//    }
//
//    @Test
//    @DisplayName("신규 사용자가 소셜 로그인을 통해 회원가입")
//    void socialLogin_NewUser_Signup() throws JsonProcessingException {
//        // given
//        String provider = "google";
//        String code = "test-code";
//        HttpServletResponse response = mock(HttpServletResponse.class);
//        User newUser = DataUtil.user2();
//
//        // Access Token 요청에 대한 RestTemplate 응답 Mock 설정
//        String accessToken = "google-access-token";
//        when(restTemplate.postForObject(any(String.class), any(), eq(Map.class)))
//                .thenReturn(Map.of("access_token", accessToken));
//
//        // UserRepository가 주어진 이메일에 해당하는 사용자를 찾지 못하도록 설정
//        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
//
//        // 새로운 사용자의 Google 사용자 정보 응답 Mock 설정
//        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(), eq(String.class)))
//                .thenReturn(new ResponseEntity<>(mockGoogleUserInfoResponse(newUser.getEmail(), newUser.getNickname()), HttpStatus.OK));
//
//        // when
//        ApiResponse<String> result = socialLoginService.socialLogin(provider, code, response);
//
//        // then
//        assertThat(result.getMessage()).isEqualTo(ApiResponseUserEnum.USER_SOCIALOGIN_SUCCESS.getMessage());
//        verify(userRepository, times(1)).save(any(User.class));
//        verify(jwtUtil, times(1)).addTokenToResponseHeader(any(), eq(response));
//    }

    @Test
    @DisplayName("유효하지 않은 소셜 로그인 제공자를 입력했을 때 예외 처리")
    void socialLogin_InvalidProvider_ExceptionThrown() {
        // given
        String provider = "invalidProvider";
        String code = "test-code";
        HttpServletResponse response = mock(HttpServletResponse.class);

        // when
        UserException exception = assertThrows(UserException.class, () -> socialLoginService.socialLogin(provider, code, response));

        // then
        assertEquals("제공하지 않는 소셜 로그인 방식입니다.", exception.getApiResponseEnum().getMessage());
    }

    /**
     * 구글 사용자 정보 응답 Mock 데이터 생성 메서드
     */
    private String mockGoogleUserInfoResponse(String email, String nickname) {
        return "{ \"email\": \"" + email + "\", \"name\": \"" + nickname + "\" }";
    }
}
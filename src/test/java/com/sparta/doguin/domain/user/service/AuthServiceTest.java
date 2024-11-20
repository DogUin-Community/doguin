package com.sparta.doguin.domain.user.service;

import com.sparta.doguin.domain.common.exception.UserException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseUserEnum;
import com.sparta.doguin.domain.user.dto.UserRequest;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;
import com.sparta.doguin.domain.user.repository.UserRepository;
import com.sparta.doguin.security.JwtUtil;
import com.sparta.doguin.security.dto.JwtUtilRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthService authService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User(
                1L,
                "test@example.com",
                "encodedPassword",
                "TestUser",
                UserType.INDIVIDUAL,
                UserRole.ROLE_USER,
                null,
                "Introduce",
                "HomeAddress",
                "GitAddress",
                "BlogAddress"
        );
    }

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() {
        // given
        UserRequest.Signup signupRequest = new UserRequest.Signup(
                "new@example.com",
                "password123!",
                "NewUser",
                "INDIVIDUAL",
                "ROLE_USER",
                null,
                "Introduce",
                "HomeAddress",
                "GitAddress",
                "BlogAddress"
        );

        given(userRepository.findByEmail(signupRequest.email())).willReturn(Optional.empty());
        given(userRepository.findByNickname(signupRequest.nickname())).willReturn(Optional.empty());
        given(passwordEncoder.encode(signupRequest.password())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(mockUser);

        // when
        ApiResponse<String> response = authService.signup(signupRequest, null);

        // then
        assertEquals(ApiResponseUserEnum.USER_CREATE_SUCCESS.getCode(), response.getCode());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 이메일")
    void signup_fail_duplicateEmail() {
        // given
        UserRequest.Signup signupRequest = new UserRequest.Signup(
                "test@example.com",
                "password123",
                "TestUser",
                "INDIVIDUAL",
                "USER",
                null,
                "Introduce",
                "HomeAddress",
                "GitAddress",
                "BlogAddress"
        );

        given(userRepository.findByEmail(signupRequest.email())).willReturn(Optional.of(mockUser));

        // when & then
        UserException exception = assertThrows(UserException.class, () -> authService.signup(signupRequest, null));
        assertEquals(ApiResponseUserEnum.USER_ALREADY_EXISTS, exception.getApiResponseEnum());
    }

    @Test
    @DisplayName("로그인 성공")
    void signin_success() {
        // given
        UserRequest.Signin signinRequest = new UserRequest.Signin(
                "test@example.com",
                "password123"
        );

        String token = "jwt-token";

        given(userRepository.findByEmail(signinRequest.email())).willReturn(Optional.of(mockUser));
        given(passwordEncoder.matches(signinRequest.password(), mockUser.getPassword())).willReturn(true);
        given(jwtUtil.createToken(any(JwtUtilRequest.CreateToken.class))).willReturn(token);

        // when
        ApiResponse<String> response = authService.signin(signinRequest, this.response);

        // then
        assertEquals(ApiResponseUserEnum.USER_LOGIN_SUCCESS.getCode(), response.getCode());
        assertEquals(token, response.getData());
        verify(jwtUtil).addTokenToResponseHeader(eq(token), eq(this.response));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void signin_fail_invalidPassword() {
        // given
        UserRequest.Signin signinRequest = new UserRequest.Signin(
                "test@example.com",
                "wrongPassword"
        );

        given(userRepository.findByEmail(signinRequest.email())).willReturn(Optional.of(mockUser));
        given(passwordEncoder.matches(signinRequest.password(), mockUser.getPassword())).willReturn(false);

        // when & then
        UserException exception = assertThrows(UserException.class, () ->
                authService.signin(signinRequest, this.response));

        assertEquals(ApiResponseUserEnum.INVALID_PASSWORD, exception.getApiResponseEnum());
    }

    @Test
    @DisplayName("로그인 실패 - 이메일 없음")
    void signin_fail_userNotFound() {
        // given
        UserRequest.Signin signinRequest = new UserRequest.Signin(
                "notfound@example.com",
                "password123"
        );

        given(userRepository.findByEmail(signinRequest.email())).willReturn(Optional.empty());

        // when & then
        UserException exception = assertThrows(UserException.class, () ->
                authService.signin(signinRequest, this.response));

        assertEquals(ApiResponseUserEnum.USER_NOT_FOUND, exception.getApiResponseEnum());
    }
}
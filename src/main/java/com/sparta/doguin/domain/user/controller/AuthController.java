package com.sparta.doguin.domain.user.controller;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.user.dto.UserRequest;
import com.sparta.doguin.domain.user.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/")
public class AuthController {
    private final AuthService authService;

    /**
     * 회원가입 요청을 처리하는 메서드
     *
     * @param signupRequest 회원가입에 필요한 정보를 담은 DTO
     * @return ResponseEntity<ApiResponse<String>> 회원가입 성공 시 JWT 토큰과 함께 응답
     * @since 1.1
     * @author 황윤서
     */
    @PostMapping("signup")
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody @Valid UserRequest.Signup signupRequest) {
        ApiResponse<String> apiResponse = authService.signup(signupRequest);
        return ApiResponse.of(apiResponse);
    }

    /**
     * 로그인 요청을 처리하는 메서드
     *
     * @param signinRequest 로그인에 필요한 정보를 담은 DTO
     * @return ResponseEntity<ApiResponse<String>> 로그인 성공 시 JWT 토큰과 함께 응답
     * @since 1.1
     * @author 황윤서
     */
    @PostMapping("signin")
    public ResponseEntity<ApiResponse<String>> signin(@RequestBody UserRequest.Signin signinRequest, HttpServletResponse response) {
        ApiResponse<String> apiResponse = authService.signin(signinRequest, response);
        return ApiResponse.of(apiResponse);
    }
}
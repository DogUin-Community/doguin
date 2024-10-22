package com.sparta.doguin.domain.user.controller;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.user.dto.request.SigninRequest;
import com.sparta.doguin.domain.user.dto.request.SignupRequest;
import com.sparta.doguin.domain.user.service.AuthService;
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

    @PostMapping("signup")
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody SignupRequest signupRequest) {
        ApiResponse<String> apiResponse = authService.signup(signupRequest);
        return ApiResponse.of(apiResponse);
    }

    @PostMapping("signin")
    public ResponseEntity<ApiResponse<String>> signin(@RequestBody SigninRequest signinRequest) {
        ApiResponse<String> apiResponse = authService.signin(signinRequest);
        return ApiResponse.of(apiResponse);
    }
}
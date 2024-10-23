package com.sparta.doguin.domain.user.service;

import com.sparta.doguin.config.JwtUtil;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseEnum;
import com.sparta.doguin.domain.common.response.ApiResponseUserEnum;
import com.sparta.doguin.domain.user.dto.request.SigninRequest;
import com.sparta.doguin.domain.user.dto.request.SignupRequest;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public ApiResponse<String> signup(SignupRequest signupRequest) {
        User newUser = new User(signupRequest.getEmail(), signupRequest.getPassword(), UserRole.of(signupRequest.getUserRole()));
        User saveduser = userRepository.save(newUser);
        ApiResponseEnum apiResponse = ApiResponseUserEnum.USER_CREATE_SUCCESS;

        return ApiResponse.of(apiResponse, jwtUtil.createToken(saveduser.getId(), saveduser.getEmail(), saveduser.getUserRole()));
    }

    @Transactional(readOnly = true)
    public ApiResponse<String> signin(SigninRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        ApiResponseEnum apiResponse = ApiResponseUserEnum.USER_LOGIN_SUCCESS;

        return ApiResponse.of(apiResponse, jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole()));
    }
}
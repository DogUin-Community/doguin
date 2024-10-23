package com.sparta.doguin.domain.user.service;

import com.sparta.doguin.config.JwtUtil;
import com.sparta.doguin.domain.common.exception.UserException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseEnum;
import com.sparta.doguin.domain.common.response.ApiResponseUserEnum;
import com.sparta.doguin.domain.user.dto.request.SigninRequest;
import com.sparta.doguin.domain.user.dto.request.SignupRequest;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;
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
        User newUser;
        // 필수 사항만 입력된 경우
        if (signupRequest.getProfileImage() == null &&
                signupRequest.getIntroduce() == null &&
                signupRequest.getHomeAddress() == null &&
                signupRequest.getGitAddress() == null &&
                signupRequest.getBlogAddress() == null) {
            newUser = new User(
                    null, // ID는 자동 생성이므로 null 전달
                    signupRequest.getEmail(),
                    signupRequest.getPassword(),
                    signupRequest.getNickname(),
                    UserType.of(signupRequest.getUserType()),
                    UserRole.of(signupRequest.getUserRole())
            );
        } else {
            // 필수 + 선택 사항 모두 입력된 경우
            newUser = new User(
                    null, // ID는 자동 생성이므로 null 전달
                    signupRequest.getEmail(),
                    signupRequest.getPassword(),
                    signupRequest.getNickname(),
                    UserType.of(signupRequest.getUserType()),
                    UserRole.of(signupRequest.getUserRole()),
                    signupRequest.getProfileImage(),
                    signupRequest.getIntroduce(),
                    signupRequest.getHomeAddress(),
                    signupRequest.getGitAddress(),
                    signupRequest.getBlogAddress()
            );
        }
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

    public User findById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ApiResponseUserEnum.USER_NOT_FOUND));
    }
}
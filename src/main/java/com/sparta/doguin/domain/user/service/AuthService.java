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
        // 중복 이메일 체크
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new UserException(ApiResponseUserEnum.USER_ALREADY_EXISTS); // 중복 회원가입 예외 처리
        }

        // 비밀번호를 암호화
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

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
                    encodedPassword,
                    signupRequest.getNickname(),
                    UserType.of(signupRequest.getUserType()),
                    UserRole.of(signupRequest.getUserRole())
            );
        } else {
            // 필수 + 선택 사항 모두 입력된 경우
            newUser = new User(
                    null, // ID는 자동 생성이므로 null 전달
                    signupRequest.getEmail(),
                    encodedPassword,
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

        return ApiResponse.of(apiResponse, jwtUtil.createToken(saveduser.getId(), saveduser.getEmail(), saveduser.getNickname(), saveduser.getUserType(), saveduser.getUserRole()));
    }

    @Transactional(readOnly = true)
    public ApiResponse<String> signin(SigninRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new UserException(ApiResponseUserEnum.USER_NOT_FOUND));

        // 비밀번호가 일치하는지 확인하기 위한 예외처리
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new UserException(ApiResponseUserEnum.INVALID_PASSWORD);
        }

        ApiResponseEnum apiResponse = ApiResponseUserEnum.USER_LOGIN_SUCCESS;

        return ApiResponse.of(apiResponse, jwtUtil.createToken(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getUserType(),
                user.getUserRole()));
    }

    public User findById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ApiResponseUserEnum.USER_NOT_FOUND));
    }
}
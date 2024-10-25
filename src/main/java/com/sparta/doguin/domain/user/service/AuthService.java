package com.sparta.doguin.domain.user.service;

import com.sparta.doguin.config.security.JwtUtil;
import com.sparta.doguin.config.security.dto.JwtUtilRequest;
import com.sparta.doguin.domain.common.exception.UserException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseEnum;
import com.sparta.doguin.domain.common.response.ApiResponseUserEnum;
import com.sparta.doguin.domain.user.dto.UserRequest;
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

    /**
     * 회원가입 요청을 처리하는 메서드
     *
     * @param signupRequest 회원가입에 필요한 정보를 담은 dto
     * @return ApiResponse<String> JWT 토큰을 포함한 회원가입 성공 메시지
     * @throws UserException 중복된 이메일이 있는 경우 예외 처리
     * @author 황윤서
     * @since 1.3
     */
    @Transactional
    public ApiResponse<String> signup(UserRequest.Signup signupRequest) {
        // 중복 이메일 체크
        if (userRepository.findByEmail(signupRequest.email()).isPresent()) {
            throw new UserException(ApiResponseUserEnum.USER_ALREADY_EXISTS); // 중복 회원가입 예외 처리
        }

        // 비밀번호를 암호화
        String encodedPassword = passwordEncoder.encode(signupRequest.password());

        User newUser = new User(
                null, // ID는 자동 생성이므로 null 전달
                signupRequest.email(),
                encodedPassword,
                signupRequest.nickname(),
                UserType.of(signupRequest.userType()),
                UserRole.of(signupRequest.userRole()),
                signupRequest.profileImage(),
                signupRequest.introduce(),
                signupRequest.homeAddress(),
                signupRequest.gitAddress(),
                signupRequest.blogAddress()
        );

        User saveduser = userRepository.save(newUser);
        ApiResponseEnum apiResponse = ApiResponseUserEnum.USER_CREATE_SUCCESS;

        JwtUtilRequest.CreateToken createToken = new JwtUtilRequest.CreateToken(
                saveduser.getId(),
                saveduser.getEmail(),
                saveduser.getNickname(),
                saveduser.getUserType(),
                saveduser.getUserRole()
        );

        return ApiResponse.of(apiResponse, jwtUtil.createToken(createToken));
    }

    /**
     * 로그인 요청을 처리하는 메서드
     *
     * @param signinRequest 로그인에 필요한 정보를 담은 DTO
     * @return ApiResponse<String> JWT 토큰을 포함한 로그인 성공 메시지
     * @throws UserException 유저가 존재하지 않거나, 비밀번호가 일치하지 않는 경우 예외 처리
     * @author 황윤서
     * @since 1.1
     */
    @Transactional(readOnly = true)
    public ApiResponse<String> signin(UserRequest.Signin signinRequest) {
        User user = userRepository.findByEmail(signinRequest.email())
                .orElseThrow(() -> new UserException(ApiResponseUserEnum.USER_NOT_FOUND));

        // 비밀번호가 일치하는지 확인하기 위한 예외처리
        if (!passwordEncoder.matches(signinRequest.password(), user.getPassword())) {
            throw new UserException(ApiResponseUserEnum.INVALID_PASSWORD);
        }

        ApiResponseEnum apiResponse = ApiResponseUserEnum.USER_LOGIN_SUCCESS;

        JwtUtilRequest.CreateToken createToken = new JwtUtilRequest.CreateToken(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getUserType(),
                user.getUserRole()
        );

        return ApiResponse.of(apiResponse, jwtUtil.createToken(createToken));
    }
}
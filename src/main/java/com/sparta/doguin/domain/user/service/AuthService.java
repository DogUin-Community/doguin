package com.sparta.doguin.domain.user.service;

import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUploadService;
import com.sparta.doguin.domain.common.exception.UserException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseEnum;
import com.sparta.doguin.domain.common.response.ApiResponseUserEnum;
import com.sparta.doguin.domain.user.dto.UserRequest;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;
import com.sparta.doguin.domain.user.repository.UserRepository;
import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.security.JwtUtil;
import com.sparta.doguin.security.dto.JwtUtilRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AttachmentUploadService attachmentUploadService;

    /**
     * 회원가입 요청을 처리하는 메서드
     *
     * @param signupRequest 회원가입에 필요한 정보를 담은 dto
     * @param files 회원가입 시 프로필 이미지 업로드할 파일
     * @return ApiResponse<String> JWT 토큰을 포함한 회원가입 성공 메시지
     * @throws UserException 중복된 이메일이 있는 경우 예외 처리
     * @author 황윤서
     * @since 1.4
     */
    @Transactional
    public ApiResponse<String> signup(UserRequest.Signup signupRequest, List<MultipartFile> files) {
        // 중복 이메일 체크
        if (userRepository.findByEmail(signupRequest.email()).isPresent()) {
            throw new UserException(ApiResponseUserEnum.USER_ALREADY_EXISTS);
        }

        // 중복 닉네임 체크
        if (userRepository.findByNickname(signupRequest.nickname()).isPresent()) {
            throw new UserException(ApiResponseUserEnum.USER_NICKNAME_ALREADY_EXISTS);
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
        AuthUser authUser = new AuthUser(saveduser.getId(),saveduser.getEmail(), saveduser.getNickname(), saveduser.getUserType(), saveduser.getUserRole());

        try {
            // 파일 업로드 시도
            attachmentUploadService.upload(files, authUser, authUser.getUserId(), AttachmentTargetType.PROFILE);
        } catch (Exception e) {
            // 파일 업로드 실패 시 예외 처리
            System.err.println("파일 업로드 실패: " + e.getMessage());
            // 필요 시 사용자에게 알림 또는 로그에 추가적인 정보를 기록할 수 있음
        }

        ApiResponseEnum apiResponse = ApiResponseUserEnum.USER_CREATE_SUCCESS;

        return ApiResponse.of(apiResponse);
    }

    /**
     * 로그인 요청을 처리하는 메서드
     *
     * @param signinRequest 로그인에 필요한 정보를 담은 DTO
     * @param response      JWT 토큰을 응답 헤더에 추가하기 위한 HttpServletResponse 객체
     * @return ApiResponse<String> JWT 토큰을 포함한 로그인 성공 메시지
     * @throws UserException 유저가 존재하지 않거나, 비밀번호가 일치하지 않는 경우 예외 처리
     * @author 황윤서
     * @since 1.1
     */
    @Transactional(readOnly = true)
    public ApiResponse<String> signin(UserRequest.Signin signinRequest, HttpServletResponse response) {
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

        String token = jwtUtil.createToken(createToken);

        // JWT를 Bearer 접두사 없이 응답 헤더에 추가
        jwtUtil.addTokenToResponseHeader(token, response);

        return ApiResponse.of(apiResponse);
    }
}
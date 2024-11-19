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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocialLoginService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final SocialLoginStrategyFactory strategyFactory;

    /**
     * 소셜 로그인 서비스를 통해 인증을 수행하는 메서드입니다.
     *
     * @param provider 소셜 로그인 제공자 이름 (kakao, naver, google, github)
     * @param code     인증 코드
     * @param response HTTP 응답 객체로, JWT 토큰을 포함하여 반환합니다.
     * @return ApiResponse<String>으로 소셜 로그인 결과를 반환합니다.
     * @throws JsonProcessingException JSON 처리에 실패할 경우 예외 발생
     * @author 황윤서
     * @since 2.0
     */
    @Transactional
    public ApiResponse<String> socialLogin(String provider, String code, HttpServletResponse response) throws JsonProcessingException {
        // String provider를 SocialProvider로 변환
        SocialProvider socialProvider;
        try {
            socialProvider = SocialProvider.valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UserException(ApiResponseUserEnum.INVALID_SOCIAL_PROVIDER);
        }

        // SocialLoginStrategy를 동적으로 가져옴
        SocialLoginStrategy strategy = strategyFactory.getStrategy(socialProvider);

        // AccessToken 가져오기
        String accessToken = strategy.getAccessToken(code, buildRedirectUri(socialProvider));

        // 사용자 정보 가져오기
        User userFromSocial = strategy.fetchUserInfo(accessToken);

        return handleUserLogin(userFromSocial, response);
    }

    /**
     * 사용자의 로그인 또는 회원가입을 처리하는 메서드입니다.
     *
     * @param userFromSocial 소셜 로그인에서 가져온 사용자 정보
     * @param response       HTTP 응답 객체
     * @return ApiResponse<String>으로 처리 결과 반환
     * @author 황윤서
     * @since 2.0
     */
    @Transactional
    public ApiResponse<String> handleUserLogin(User userFromSocial, HttpServletResponse response) {
        Optional<User> existingUserOpt = userRepository.findByEmail(userFromSocial.getEmail());

        if (existingUserOpt.isPresent()) {
            // 기존 유저가 있을 경우 JWT를 생성하여 반환
            User existingUser = existingUserOpt.get();
            String jwt = addJwtToResponse(existingUser, response);
            return ApiResponse.of(ApiResponseUserEnum.USER_SOCIALOGIN_SUCCESS, jwt);
        } else {
            // 신규 유저 등록
            User newUser = User.builder()
                    .email(userFromSocial.getEmail())
                    .nickname(userFromSocial.getNickname())
                    .password(null) // 소셜 로그인 사용자라 비밀번호는 null
                    .userType(UserType.INDIVIDUAL)
                    .userRole(UserRole.ROLE_USER)
                    .build();

            userRepository.save(newUser);

            // JWT 생성 및 반환
            String jwt = addJwtToResponse(newUser, response);
            return ApiResponse.of(ApiResponseUserEnum.USER_SOCIALOGIN_SUCCESS, jwt);
        }
    }

    /**
     * JWT 토큰을 생성하고 응답 헤더에 추가하는 메서드입니다.
     *
     * @param user     사용자 정보
     * @param response HTTP 응답 객체
     * @return 생성된 JWT 토큰
     */
    private String addJwtToResponse(User user, HttpServletResponse response) {
        JwtUtilRequest.CreateToken createToken = new JwtUtilRequest.CreateToken(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getUserType(),
                user.getUserRole()
        );

        String jwt = jwtUtil.createToken(createToken);
        jwtUtil.addTokenToResponseHeader(jwt, response);
        return jwt;
    }

    /**
     * Redirect URI를 생성합니다.
     *
     * @param provider 소셜 로그인 제공자
     * @return Redirect URI
     */
    private String buildRedirectUri(SocialProvider provider) {
        return "http://doguin-alb-1242367005.ap-northeast-2.elb.amazonaws.com/api/v1/auth/oauth2/authorize/" + provider.getProviderName();
    }
}
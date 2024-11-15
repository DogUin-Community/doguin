package com.sparta.doguin.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.doguin.domain.common.exception.UserException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseUserEnum;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;
import com.sparta.doguin.domain.user.repository.UserRepository;
import com.sparta.doguin.security.JwtUtil;
import com.sparta.doguin.security.dto.JwtUtilRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialLoginService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;

    @Value("${social.kakao.client-id}")
    private String kakaoClientId;

    @Value("${social.naver.client-id}")
    private String naverClientId;

    @Value("${social.naver.client-secret}")
    private String naverClientSecret;

    @Value("${social.google.client-id}")
    private String googleClientId;

    @Value("${social.google.client-secret}")
    private String googleClientSecret;

    @Value("${social.github.client-id}")
    private String githubClientId;

    @Value("${social.github.client-secret}")
    private String githubClientSecret;

    /**
     * 소셜 로그인 서비스를 통해 인증을 수행하는 메서드입니다.
     *
     * @param provider 소셜 로그인 제공자 이름 (kakao, naver, google, github)
     * @param code     인증 코드
     * @param response HTTP 응답 객체로, JWT 토큰을 포함하여 반환합니다.
     * @return ApiResponse<String>으로 소셜 로그인 결과를 반환합니다.
     * @throws JsonProcessingException JSON 처리에 실패할 경우 예외 발생
     * @author 황윤서
     * @since 1.0
     */
    @Transactional
    public ApiResponse<String> socialLogin(String provider, String code, HttpServletResponse response) throws JsonProcessingException {
        String accessToken = getAccessToken(provider, code);

        // accessToken을 사용해 사용자 정보를 가져온 후 handleUserLogin에 전달
        User userFromSocial = fetchUserInfoFromProvider(accessToken, provider);
        return handleUserLogin(userFromSocial, response, null);
    }

    /**
     * AccessToken을 발급받기 위한 공통 메서드입니다.
     * 소셜 로그인 제공자의 토큰 엔드포인트와 클라이언트 자격 증명을 사용하여 AccessToken을 요청합니다.
     *
     * @param provider 소셜 로그인 제공자 이름 (kakao, naver, google, github)
     * @param code     인증 코드
     * @return 발급받은 AccessToken을 반환합니다.
     * @throws UserException 잘못된 provider 또는 AccessToken 발급 실패 시 예외 발생
     * @author 황윤서
     * @since 1.0
     */
    private String getAccessToken(String provider, String code) {
        String redirectUri = "http://localhost:8080/api/v1/auth/oauth2/authorize/" + provider;
        String url;
        String clientId;
        String clientSecret = null;  // 필요한 경우에만 할당

        // provider에 따른 URL, clientId, clientSecret 설정
        switch (provider) {
            case "kakao":
                url = "https://kauth.kakao.com/oauth/token";
                clientId = kakaoClientId;
                break;
            case "naver":
                url = "https://nid.naver.com/oauth2.0/token";
                clientId = naverClientId;
                clientSecret = naverClientSecret;
                break;
            case "google":
                url = "https://oauth2.googleapis.com/token";
                clientId = googleClientId;
                clientSecret = googleClientSecret;
                break;
            case "github":
                url = "https://github.com/login/oauth/access_token";
                clientId = githubClientId;
                clientSecret = githubClientSecret;
                break;
            default:
                throw new UserException(ApiResponseUserEnum.INVALID_SOCIAL_PROVIDER);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        if ("github".equals(provider)) {
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON)); // 깃허브 JSON 응답 설정
        }

        // 파라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        // clientSecret이 존재하는 경우에만 추가
        if (clientSecret != null) {
            params.add("client_secret", clientSecret);
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        Map<String, Object> responseBody = restTemplate.postForObject(url, request, Map.class);

        if (responseBody == null || !responseBody.containsKey("access_token")) {
            log.error("Failed to fetch access token from {} provider. Response: {}", provider, responseBody);
            throw new UserException(ApiResponseUserEnum.FAILED_TO_FETCH_SOCIAL_ACCESS_TOKEN);
        }

        return (String) responseBody.get("access_token");
    }


    /**
     * 소셜 로그인한 사용자 정보로 로그인/회원가입을 처리하는 메서드입니다.
     *
     * @param userFromSocial 소셜 로그인에서 가져온 사용자 정보
     * @param response       HTTP 응답 객체로, JWT 토큰을 포함하여 반환합니다.
     * @param rawPassword    비밀번호 (기존 사용자인 경우에만 필요)
     * @return ApiResponse<String>으로 로그인 처리 결과를 반환합니다.
     * @author 황윤서
     * @since 1.0
     */
    @Transactional
    public ApiResponse<String> handleUserLogin(User userFromSocial, HttpServletResponse response, String rawPassword) {
        // 같은 이메일이 있는지 확인
        Optional<User> existingUserOpt = userRepository.findByEmail(userFromSocial.getEmail());
        String jwt;
        if (existingUserOpt.isPresent()) {
            switch ((existingUserOpt.get().getEmail())) {
                case "kakao.com":
                    // 카카오 처리 로직
                    User savedUser1 = userRepository.save(existingUserOpt.get());
                    jwt = addJwtToResponse(savedUser1, response);
                    return ApiResponse.of(ApiResponseUserEnum.USER_SOCIALOGIN_SUCCESS,jwt);
                case "naver.com":
                    // 네이버 처리 로직
                    User savedUser2 = userRepository.save(existingUserOpt.get());
                    jwt = addJwtToResponse(savedUser2, response);
                    return ApiResponse.of(ApiResponseUserEnum.USER_SOCIALOGIN_SUCCESS,jwt);
                case "google.com":
                    // 구글 처리 로직
                    User savedUser3 = userRepository.save(existingUserOpt.get());
                    jwt = addJwtToResponse(savedUser3, response);
                    return ApiResponse.of(ApiResponseUserEnum.USER_SOCIALOGIN_SUCCESS,jwt);
                case "github.com":
                    // 깃허브 처리 로직
                    User savedUser4 = userRepository.save(existingUserOpt.get());
                    jwt = addJwtToResponse(savedUser4, response);
                    return ApiResponse.of(ApiResponseUserEnum.USER_SOCIALOGIN_SUCCESS,jwt);
            }

            // 비밀번호가 일치하는 경우, 기존 계정과 연동하여 소셜 로그인 진행
            User existingUser = existingUserOpt.get();
            jwt = addJwtToResponse(existingUser, response);
            return ApiResponse.of(ApiResponseUserEnum.USER_SOCIALOGIN_SUCCESS,jwt);
        } else {
            // 기존 사용자가 없는 경우 신규 사용자로 등록
            User newUser = User.builder()
                    .email(userFromSocial.getEmail())
                    .nickname(userFromSocial.getNickname())
                    .password(null)
                    .userType(UserType.INDIVIDUAL)
                    .userRole(UserRole.ROLE_USER)
                    .build();

            User savedUser = userRepository.save(newUser);

            // JWT 생성 및 헤더 추가

            String savedJwt = addJwtToResponse(savedUser, response);
            return ApiResponse.of(ApiResponseUserEnum.USER_SOCIALOGIN_SUCCESS,savedJwt);
        }
    }

    /**
     * JWT 토큰을 생성하고, 응답 헤더에 추가하는 메서드입니다.
     *
     * @param user     토큰을 생성할 사용자 정보
     * @param response HTTP 응답 객체로, JWT 토큰을 포함하여 반환합니다.
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
     * AccessToken을 사용하여 소셜 제공자의 사용자 정보를 가져오는 메서드입니다.
     *
     * @param accessToken 소셜 제공자에서 발급받은 액세스 토큰
     * @param provider    소셜 로그인 제공자 이름
     * @return User 객체로 사용자 정보를 반환합니다.
     * @throws JsonProcessingException JSON 처리에 실패할 경우 예외 발생
     */
    private User fetchUserInfoFromProvider(String accessToken, String provider) throws JsonProcessingException {
        switch (provider) {
            case "kakao":
                return fetchKakaoUserInfo(accessToken);
            case "naver":
                return fetchNaverUserInfo(accessToken);
            case "google":
                return fetchGoogleUserInfo(accessToken);
            case "github":
                return fetchGitHubUserInfo(accessToken);
            default:
                throw new UserException(ApiResponseUserEnum.INVALID_SOCIAL_PROVIDER);
        }
    }

    /**
     * 카카오 사용자 정보를 가져오는 메서드입니다.
     *
     * @param accessToken 카카오에서 발급받은 액세스 토큰
     * @return User 객체로 사용자 정보를 반환합니다.
     * @throws JsonProcessingException JSON 처리에 실패할 경우 예외 발생
     */
    private User fetchKakaoUserInfo(String accessToken) throws JsonProcessingException {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>(headers);
        String responseBody = restTemplate.exchange(url, HttpMethod.GET, request, String.class).getBody();

        if (responseBody == null) {
            log.error("Kakao API response body is null.");
            throw new UserException(ApiResponseUserEnum.FAILED_TO_FETCH_SOCIAL_USER_INFO);
        }

        JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();

        User user = new User();
        user.socialLogin(email, nickname);
        return user;
    }

    /**
     * 네이버 사용자 정보를 가져오는 메서드입니다.
     *
     * @param accessToken 네이버에서 발급받은 액세스 토큰
     * @return User 객체로 사용자 정보를 반환합니다.
     * @throws JsonProcessingException JSON 처리에 실패할 경우 예외 발생
     */
    private User fetchNaverUserInfo(String accessToken) throws JsonProcessingException {
        String url = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        String responseBody = restTemplate.exchange(url, HttpMethod.GET, request, String.class).getBody();

        if (responseBody == null) {
            log.error("Naver API response body is null.");
            throw new UserException(ApiResponseUserEnum.FAILED_TO_FETCH_SOCIAL_USER_INFO);
        }

        JsonNode jsonNode = new ObjectMapper().readTree(responseBody).get("response");
        String email = jsonNode.get("email").asText();
        String nickname = jsonNode.get("nickname").asText();

        User user = new User();
        user.socialLogin(email, nickname);
        return user;
    }

    /**
     * 구글 사용자 정보를 가져오는 메서드입니다.
     *
     * @param accessToken 구글에서 발급받은 액세스 토큰
     * @return User 객체로 사용자 정보를 반환합니다.
     * @throws JsonProcessingException JSON 처리에 실패할 경우 예외 발생
     */
    private User fetchGoogleUserInfo(String accessToken) throws JsonProcessingException {
        String url = "https://www.googleapis.com/oauth2/v3/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        String responseBody = restTemplate.exchange(url, HttpMethod.GET, request, String.class).getBody();

        if (responseBody == null) {
            log.error("Google API response body is null.");
            throw new UserException(ApiResponseUserEnum.FAILED_TO_FETCH_SOCIAL_USER_INFO);
        }

        JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
        String email = jsonNode.get("email").asText();
        String nickname = jsonNode.get("name").asText();

        User user = new User();
        user.socialLogin(email, nickname);
        return user;
    }

    /**
     * 깃헙 사용자 정보를 가져오는 메서드입니다.
     *
     * @param accessToken 깃헙에서 발급받은 액세스 토큰
     * @return User 객체로 사용자 정보를 반환합니다.
     * @throws JsonProcessingException JSON 처리에 실패할 경우 예외 발생
     */
    private User fetchGitHubUserInfo(String accessToken) throws JsonProcessingException {
        String url = "https://api.github.com/user";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        String responseBody = restTemplate.exchange(url, HttpMethod.GET, request, String.class).getBody();

        if (responseBody == null) {
            log.error("GitHub API response body is null.");
            throw new UserException(ApiResponseUserEnum.FAILED_TO_FETCH_SOCIAL_USER_INFO);
        }

        JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
        String nickname = jsonNode.get("login").asText();
        String email = nickname + "@github.com";

        User user = new User();
        user.socialLogin(email, nickname);
        return user;
    }
}
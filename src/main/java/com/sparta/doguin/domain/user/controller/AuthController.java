package com.sparta.doguin.domain.user.controller;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.user.dto.UserRequest;
import com.sparta.doguin.domain.user.service.AuthService;
import com.sparta.doguin.domain.user.service.SocialLoginService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final SocialLoginService socialLoginService;

    /**
     * 회원가입 요청을 처리하는 메서드
     *
     * @param signupRequest 회원가입에 필요한 정보를 담은 DTO
     * @param files 회원가입 시 프로필 이미지 업로드할 파일
     * @return ResponseEntity<ApiResponse<String>> 회원가입 성공 시 JWT 토큰과 함께 응답
     * @since 1.2
     * @author 황윤서
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(
            @RequestPart(name = "signupRequest") @Valid UserRequest.Signup signupRequest,
            @RequestPart(name = "files", required = false) List<MultipartFile> files
    ) {
        ApiResponse<String> apiResponse = authService.signup(signupRequest,files);
        return ApiResponse.of(apiResponse);
    }

    /**
     * 로그인 요청을 처리하는 메서드
     *
     * @param signinRequest 로그인에 필요한 정보를 담은 DTO
     * @return ResponseEntity<ApiResponse < String>> 로그인 성공 시 JWT 토큰과 함께 응답
     * @author 황윤서
     * @since 1.1
     */
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<String>> signin(@RequestBody UserRequest.Signin signinRequest, HttpServletResponse response) {
        ApiResponse<String> apiResponse = authService.signin(signinRequest, response);
        return ApiResponse.of(apiResponse);
    }

    @GetMapping("/oauth2/authorize/{provider}")
    public ResponseEntity<ApiResponse<String>> socialLogin(
            @PathVariable("provider") String provider,
            @RequestParam("code") String code,           // OAuth 인증 코드
            @RequestParam("state") String frontHost,     // 원래의 프론트엔드 호스트 정보
            HttpServletResponse response) throws IOException {

        // 소셜 로그인 처리 후 토큰 생성
        ApiResponse<String> apiResponse = socialLoginService.socialLogin(provider, code, response);

        // 프론트엔드로 리다이렉트
        response.sendRedirect(frontHost + "/callback?token=" + apiResponse.getData());
        return ApiResponse.of(apiResponse);
    }
}
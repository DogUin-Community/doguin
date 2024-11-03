package com.sparta.doguin.domain.mypage.controller;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseMypageEnum;
import com.sparta.doguin.domain.mypage.dto.MypageResponse;
import com.sparta.doguin.domain.mypage.service.MypageService;
import com.sparta.doguin.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mypages")
public class MypageController {
    private final MypageService mypageService;

    // 마이페이지 조회
    @GetMapping
    public ResponseEntity<ApiResponse<MypageResponse.Mypage>> getMypage(@AuthenticationPrincipal AuthUser authUser) {
        MypageResponse.Mypage response = mypageService.getMypage(authUser);
        return ApiResponse.of(ApiResponse.of(ApiResponseMypageEnum.MYPAGE_GET_SUCCESS, response));
    }
}
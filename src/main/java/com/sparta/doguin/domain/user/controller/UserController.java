package com.sparta.doguin.domain.user.controller;

import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.user.dto.UserRequest;
import com.sparta.doguin.domain.user.dto.UserResponse;
import com.sparta.doguin.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    // 회원 정보 조회
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<UserResponse.Check>> checkUser(@AuthenticationPrincipal AuthUser authUser) {
        ApiResponse<UserResponse.Check> response = userService.checkUser(authUser);
        return ApiResponse.of(response);
    }

    // 회원 정보 수정
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<UserResponse.Update>> updateUser(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody UserRequest.Update update) {
        ApiResponse<UserResponse.Update> response = userService.updateUser(authUser, update);
        return ApiResponse.of(response);
    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @AuthenticationPrincipal AuthUser authUser) {
        ApiResponse<Void> response = userService.deleteUser(authUser);
        return ApiResponse.of(response);
    }
}

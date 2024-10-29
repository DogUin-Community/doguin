package com.sparta.doguin.domain.follow.controller;

import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.follow.dto.FollowResponse;
import com.sparta.doguin.domain.follow.service.FollowService;
import com.sparta.doguin.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/follows")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    // 팔로우 추가
    @PostMapping
    public ResponseEntity<ApiResponse<FollowResponse.Follow>> followUser(@AuthenticationPrincipal AuthUser authUser, @RequestParam("followedEmail") String followedEmail) {
        User user = getAuthenticatedUser(authUser);
        ApiResponse<FollowResponse.Follow> response = followService.follow(user.getId(), followedEmail);
        return ApiResponse.of(response);
    }

    // 나를 팔로우한 사용자 목록 조회
    @GetMapping("/followers")
    public ResponseEntity<ApiResponse<List<FollowResponse.Follow>>> getFollowers(@AuthenticationPrincipal AuthUser authUser) {
        User user = getAuthenticatedUser(authUser);
        ApiResponse<List<FollowResponse.Follow>> response = followService.getFollowers(user.getId());
        return ApiResponse.of(response);
    }

    // 팔로우한 사용자 목록 조회(내가 팔로우한 사용자들)
    @GetMapping("/followings")
    public ResponseEntity<ApiResponse<List<FollowResponse.Follow>>> getFollowedUsers(@AuthenticationPrincipal AuthUser authUser) {
        User user = getAuthenticatedUser(authUser);
        ApiResponse<List<FollowResponse.Follow>> response = followService.getFollowedUsers(user.getId());
        return ApiResponse.of(response);
    }

    // 팔로우 해제
    @DeleteMapping("/{followedEmail}")
    public ResponseEntity<ApiResponse<FollowResponse.Follow>> unfollowUser(@AuthenticationPrincipal AuthUser authUser, @PathVariable("followedEmail") String followedEmail) {
        User user = getAuthenticatedUser(authUser);
        ApiResponse<FollowResponse.Follow> response = followService.unfollow(user.getId(), followedEmail);
        return ApiResponse.of(response);
    }

    // 로그인된 사용자의 인증 정보를 User 엔티티로 변환하는 메서드
    private User getAuthenticatedUser(AuthUser authUser) {
        return User.fromAuthUser(authUser);
    }
}
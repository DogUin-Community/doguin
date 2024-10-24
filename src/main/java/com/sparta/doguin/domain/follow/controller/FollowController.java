package com.sparta.doguin.domain.follow.controller;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.follow.dto.FollowResponse;
import com.sparta.doguin.domain.follow.service.FollowService;
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
        Long followerId = authUser.getUserId();  // 로그인한 사용자의 ID를 AuthUser에서 가져옴
        ApiResponse<FollowResponse.Follow> response = followService.follow(followerId, followedEmail);
        return ApiResponse.of(response);
    }

    // 나를 팔로우한 사용자 목록 조회
    @GetMapping("/followers")
    public ResponseEntity<ApiResponse<List<FollowResponse.Follow>>> getFollowers(@AuthenticationPrincipal AuthUser authUser) {
        Long userId = authUser.getUserId();
        ApiResponse<List<FollowResponse.Follow>> response = followService.getFollowers(userId);
        return ApiResponse.of(response);
    }

    // 팔로우한 사용자 목록 조회(내가 팔로우한 사용자들)
    @GetMapping("/followings")
    public ResponseEntity<ApiResponse<List<FollowResponse.Follow>>> getFollowedUsers(@AuthenticationPrincipal AuthUser authUser) {
        Long userId = authUser.getUserId();
        ApiResponse<List<FollowResponse.Follow>> response = followService.getFollowedUsers(userId);
        return ApiResponse.of(response);
    }

    // 팔로우 해제
    @DeleteMapping("/{followedEmail}")
    public ResponseEntity<ApiResponse<FollowResponse.Follow>> unfollowUser(@AuthenticationPrincipal AuthUser authUser, @PathVariable("followedEmail") String followedEmail) {
        Long followerId = authUser.getUserId();
        ApiResponse<FollowResponse.Follow> response = followService.unfollow(followerId, followedEmail);
        return ApiResponse.of(response);
    }
}

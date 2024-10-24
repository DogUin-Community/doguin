package com.sparta.doguin.domain.follow.dto;

public sealed interface FollowResponse permits FollowResponse.Follow {
    record Follow(Long userId, String email) implements FollowResponse {}
}
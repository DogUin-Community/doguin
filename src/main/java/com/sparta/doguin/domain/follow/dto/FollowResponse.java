package com.sparta.doguin.domain.follow.dto;

public sealed interface FollowResponse permits FollowResponse.Follow, FollowResponse.FollowOnlyId {
    record Follow(Long userId, String email) implements FollowResponse {}
    record FollowOnlyId(Long userId) implements FollowResponse {}
}
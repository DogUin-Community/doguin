package com.sparta.doguin.domain.discussions.dto;

public sealed interface DiscussionRequest permits DiscussionRequest.CreateRequest, DiscussionRequest.UpdateRequest {
    record CreateRequest(String title, String content, Long userId) implements DiscussionRequest {}
    record UpdateRequest(String title, String content, Long userId) implements DiscussionRequest {}
}

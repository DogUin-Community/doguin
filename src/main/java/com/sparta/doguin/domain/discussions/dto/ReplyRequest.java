package com.sparta.doguin.domain.discussions.dto;

public sealed interface ReplyRequest permits ReplyRequest.CreateRequest, ReplyRequest.UpdateRequest {
    record CreateRequest(String content, Long userId) implements ReplyRequest {}
    record UpdateRequest(String content) implements ReplyRequest {}
}

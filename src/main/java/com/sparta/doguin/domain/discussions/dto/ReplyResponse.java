package com.sparta.doguin.domain.discussions.dto;

import java.util.List;

public sealed interface ReplyResponse permits ReplyResponse.AddReplyResponse,ReplyResponse.SingleResponse, ReplyResponse.ListResponse {
    record AddReplyResponse(Long id, String content, String nickname, String createdAt) implements ReplyResponse {}
    record SingleResponse(Long id, String content, String nickname, String createdAt, String updatedAt) implements ReplyResponse {}

    record ListResponse(Long id, String content, String nickname, String createdAt,
                        List<ListResponse> nestedReplies) implements ReplyResponse {}
}
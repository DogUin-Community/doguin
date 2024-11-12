package com.sparta.doguin.domain.discussions.dto;

import java.io.Serializable;
import java.util.List;

public sealed interface ReplyResponse permits ReplyResponse.AddReplyResponse, ReplyResponse.SingleResponse, ReplyResponse.ListResponse {
    record AddReplyResponse(Long id, String content, String nickname, String createdAt, List<DiscussionAttachmentResponse> attachments) implements ReplyResponse, Serializable {}
    record SingleResponse(Long id, String content, String nickname, String createdAt, String updatedAt, List<DiscussionAttachmentResponse> attachments) implements ReplyResponse, Serializable {}
    record ListResponse(Long id, String content, String nickname, String createdAt, List<ListResponse> nestedReplies) implements ReplyResponse, Serializable {}
}
package com.sparta.doguin.domain.discussions.dto;


import java.io.Serializable;
import java.util.List;

public sealed interface DiscussionResponse permits DiscussionResponse.SingleResponse, DiscussionResponse.ListResponse {
    record SingleResponse(Long id, String title, String content, String nickname, int viewCount, String status,
                          String createdAt, String updatedAt,
                          List<ReplyResponse.SingleResponse> replies,
                          List<DiscussionAttachmentResponse> attachments) implements DiscussionResponse, Serializable {}

    record ListResponse(Long id, String title, String content, String nickname, int viewCount, String status,
                        String createdAt, String updatedAt,
                        List<DiscussionAttachmentResponse> attachments) implements DiscussionResponse, Serializable {}
}
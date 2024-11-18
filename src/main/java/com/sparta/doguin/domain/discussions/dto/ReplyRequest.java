package com.sparta.doguin.domain.discussions.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public sealed interface ReplyRequest permits ReplyRequest.CreateRequest, ReplyRequest.UpdateRequest {
    record CreateRequest(String content, Long userId, List<MultipartFile> attachments) implements ReplyRequest {}
    record UpdateRequest(String content, List<MultipartFile> attachments) implements ReplyRequest {}
}

package com.sparta.doguin.domain.discussions.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public sealed interface DiscussionRequest permits DiscussionRequest.CreateRequest, DiscussionRequest.UpdateRequest {
    record CreateRequest(String title, String content, Long userId, List<MultipartFile> attachments) implements DiscussionRequest {}
    record UpdateRequest(String title, String content, Long userId, List<MultipartFile> attachments) implements DiscussionRequest {}
}

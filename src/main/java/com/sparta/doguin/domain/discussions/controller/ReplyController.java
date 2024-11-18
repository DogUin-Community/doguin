package com.sparta.doguin.domain.discussions.controller;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.discussions.dto.ReplyRequest;
import com.sparta.doguin.domain.discussions.dto.ReplyResponse;
import com.sparta.doguin.domain.discussions.service.ReplyService;
import com.sparta.doguin.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discussions/{discussionId}/replies")
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReplyResponse.AddReplyResponse>> addReply(
            @PathVariable Long discussionId,
            @RequestPart("attachments") List<MultipartFile> attachments,
            @RequestPart("request") ReplyRequest.CreateRequest request,
            @AuthenticationPrincipal AuthUser authUser) {
        return ApiResponse.of(replyService.addReply(discussionId, attachments, request, authUser));
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<ApiResponse<Void>> deleteReply(
            @PathVariable Long replyId,
            @AuthenticationPrincipal AuthUser authUser) {
        return ApiResponse.of(replyService.deleteReply(replyId, authUser));
    }

    @PutMapping("/{replyId}")
    public ResponseEntity<ApiResponse<ReplyResponse.SingleResponse>> updateReply(
            @PathVariable Long replyId,
            @RequestPart(value = "newAttachments", required = false) List<MultipartFile> newAttachments,
            @RequestPart(value = "attachmentIdsToDelete", required = false) List<Long> attachmentIdsToDelete,
            @RequestPart("request") ReplyRequest.UpdateRequest request,
            @AuthenticationPrincipal AuthUser authUser) {
        return ApiResponse.of(replyService.updateReply(replyId, newAttachments, attachmentIdsToDelete, request, authUser));
    }
}

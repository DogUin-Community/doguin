package com.sparta.doguin.domain.discussions.controller;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.discussions.dto.ReplyRequest;
import com.sparta.doguin.domain.discussions.dto.ReplyResponse;
import com.sparta.doguin.domain.discussions.service.ReplyService;
import com.sparta.doguin.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/discussions/{discussionId}/replies")
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReplyResponse.AddReplyResponse>> addReply(
            @PathVariable Long discussionId,
            @RequestBody ReplyRequest.CreateRequest request,
            @AuthenticationPrincipal AuthUser authUser) {
        return ApiResponse.of(replyService.addReply(discussionId, request, authUser));
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
            @RequestBody ReplyRequest.UpdateRequest request,
            @AuthenticationPrincipal AuthUser authUser) {
        return ApiResponse.of(replyService.updateReply(replyId, request, authUser));
    }
}

package com.sparta.doguin.domain.discussions.controller;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseDiscussionEnum;
import com.sparta.doguin.domain.discussions.dto.DiscussionRequest;
import com.sparta.doguin.domain.discussions.dto.DiscussionResponse;
import com.sparta.doguin.domain.discussions.service.DiscussionService;
import com.sparta.doguin.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/discussions")
public class DiscussionController {

    private final DiscussionService discussionService;

    @PostMapping
    public ResponseEntity<ApiResponse<DiscussionResponse.SingleResponse>> createDiscussion(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody DiscussionRequest.CreateRequest request) {
        return ApiResponse.of(discussionService.createDiscussion(authUser, request));
    }

    @GetMapping("/{discussionId}")
    public ResponseEntity<ApiResponse<DiscussionResponse.SingleResponse>> getDiscussion(
            @PathVariable Long discussionId,
            @AuthenticationPrincipal AuthUser authUser) {
        return ApiResponse.of(ApiResponse.of(ApiResponseDiscussionEnum.DISCUSSION_FETCH_SUCCESS,discussionService.getDiscussion(discussionId, authUser)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DiscussionResponse.ListResponse>>> getAllDiscussions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sort), "createdAt");
        return ApiResponse.of(ApiResponse.of(ApiResponseDiscussionEnum.DISCUSSION_LIST_FETCH_SUCCESS, discussionService.getAllDiscussions(pageable)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<DiscussionResponse.ListResponse>>> searchDiscussions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String nickname) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sort), "createdAt");
        return ApiResponse.of(ApiResponse.of(ApiResponseDiscussionEnum.DISCUSSION_LIST_FETCH_SUCCESS, discussionService.searchDiscussions(pageable, title, content, nickname)));
    }

    @PutMapping("/{discussionId}")
    public ResponseEntity<ApiResponse<DiscussionResponse.SingleResponse>> updateDiscussion(
            @PathVariable Long discussionId,
            @RequestBody DiscussionRequest.UpdateRequest request,
            @AuthenticationPrincipal AuthUser authUser) {
        return ApiResponse.of(ApiResponse.of(ApiResponseDiscussionEnum.DISCUSSION_UPDATE_SUCCESS, discussionService.updateDiscussion(discussionId, request, authUser)));
    }

    @DeleteMapping("/{discussionId}")
    public ResponseEntity<ApiResponse<Void>> deleteDiscussion(
            @PathVariable Long discussionId,
            @AuthenticationPrincipal AuthUser authUser) {
        discussionService.deleteDiscussion(discussionId, authUser);
        return ApiResponse.of(ApiResponse.of(ApiResponseDiscussionEnum.DISCUSSION_DELETE_SUCCESS));
    }
}

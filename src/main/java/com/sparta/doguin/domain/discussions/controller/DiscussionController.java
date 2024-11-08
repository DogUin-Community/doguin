package com.sparta.doguin.domain.discussions.controller;

import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentDeleteService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUploadService;
import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import com.sparta.doguin.domain.bookmark.model.BookmarkRequest;
import com.sparta.doguin.domain.bookmark.service.BookmarkService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseBookmarkEnum;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discussions")
public class DiscussionController {

    private final DiscussionService discussionService;
    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<ApiResponse<DiscussionResponse.SingleResponse>> createDiscussion(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments,
            @RequestPart("request") DiscussionRequest.CreateRequest request) {
        return ApiResponse.of(discussionService.createDiscussion(authUser, attachments, request));
    }

    @GetMapping("/{discussionId}")
    public ResponseEntity<ApiResponse<DiscussionResponse.SingleResponse>> getDiscussion(
            @PathVariable Long discussionId,
            @AuthenticationPrincipal AuthUser authUser) {
        return ApiResponse.of(ApiResponse.of(ApiResponseDiscussionEnum.DISCUSSION_FETCH_SUCCESS, discussionService.getDiscussion(discussionId, authUser)));
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
            @RequestPart("request") DiscussionRequest.UpdateRequest request,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestPart(value = "attachmentIdsToDelete", required = false) List<Long> attachmentIdsToDelete,
            @RequestPart(value = "newAttachments", required = false) List<MultipartFile> newAttachments) {

        DiscussionResponse.SingleResponse response = discussionService.updateDiscussion(discussionId, request, authUser, attachmentIdsToDelete, newAttachments);
        return ApiResponse.of(ApiResponse.of(ApiResponseDiscussionEnum.DISCUSSION_UPDATE_SUCCESS, response));
    }

    @DeleteMapping("/{discussionId}")
    public ResponseEntity<ApiResponse<Void>> deleteDiscussion(
            @PathVariable Long discussionId,
            @AuthenticationPrincipal AuthUser authUser) {
        discussionService.deleteDiscussion(discussionId, authUser);
        return ApiResponse.of(ApiResponse.of(ApiResponseDiscussionEnum.DISCUSSION_DELETE_SUCCESS));
    }

    @PostMapping("/{discussionId}/bookmark")
    public ResponseEntity<ApiResponse<Void>> bookmarkDiscussion(
            @PathVariable Long discussionId,
            @AuthenticationPrincipal AuthUser authUser) {
        BookmarkRequest.BookmarkRequestCreate reqDto = new BookmarkRequest.BookmarkRequestCreate(discussionId, BookmarkTargetType.DISCUSSION);
        bookmarkService.createBookmark(reqDto, authUser);
        ApiResponse<Void> response = ApiResponse.of(ApiResponseBookmarkEnum.BOOKMARK_OK);
        return ResponseEntity.status(ApiResponseBookmarkEnum.BOOKMARK_OK.getHttpStatus()).body(response);
    }
}

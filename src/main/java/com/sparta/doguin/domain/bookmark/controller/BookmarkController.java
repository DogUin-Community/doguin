package com.sparta.doguin.domain.bookmark.controller;

import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import com.sparta.doguin.domain.bookmark.model.BookmarkRequest;
import com.sparta.doguin.domain.bookmark.model.BookmarkResponse;
import com.sparta.doguin.domain.bookmark.service.BookmarkService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "북마크 API",description = "북마크 관련된 API를 확인 할 수 있습니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    /**
     * 로그인 되있는 자신의 북마크들 목록 확인
     */
    @Operation(summary = "자신의 모든 북마크들 가져오기", description = "자신의 북마크 다건 조회 API")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BookmarkResponse>>> getAllBookmarks(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "desc", required = false) String sort,
            @RequestParam(required = false) BookmarkTargetType target,
            @AuthenticationPrincipal AuthUser authUser
            ) {
        Sort.Direction direction = Sort.Direction.fromString(sort);
        Pageable pageable = PageRequest.of(page, size, direction,"createdAt");
        ApiResponse<Page<BookmarkResponse>> apiResponse = bookmarkService.getAllBookmarksByUser(pageable, authUser, target);
        return ApiResponse.of(apiResponse);
    }

    @Operation(summary = "북마크 생성", description = "북마크 생성 API")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createBookmark(@RequestBody BookmarkRequest.BookmarkRequestCreate reqDto, @AuthenticationPrincipal AuthUser authUser){
        ApiResponse<Void> apiResponse = bookmarkService.createBookmark(reqDto,authUser);
        return ApiResponse.of(apiResponse);
    }

    @Operation(summary = "북마크 삭제", description = "북마크 삭제 API")
    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<ApiResponse<Void>> deleteBookmark(@PathVariable Long bookmarkId, @AuthenticationPrincipal AuthUser authUser){
        ApiResponse<Void> apiResponse = bookmarkService.deleteBookmark(bookmarkId,authUser);
        return ApiResponse.of(apiResponse);
    }

}

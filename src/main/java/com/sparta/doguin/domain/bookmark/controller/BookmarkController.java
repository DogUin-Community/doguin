package com.sparta.doguin.domain.bookmark.controller;

import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import com.sparta.doguin.domain.bookmark.model.BookmarkRequest;
import com.sparta.doguin.domain.bookmark.model.BookmarkRequestSearch;
import com.sparta.doguin.domain.bookmark.model.BookmarkResponse;
import com.sparta.doguin.domain.bookmark.service.BookmarkService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "북마크 API",description = "북마크 관련된 API를 확인 할 수 있습니다")
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    /**
     * 로그인 되있는 자신의 북마크들 목록 확인 하기
     */
    @Operation(summary = "자신의 모든 북마크들 가져오기", description = "자신의 북마크 다건 조회 API")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BookmarkResponse>>> getAllBookmarks(
            @ModelAttribute @Valid BookmarkRequestSearch request,
            @AuthenticationPrincipal AuthUser authUser
    ){
        Sort.Direction direction = Sort.Direction.fromString(request.getSort());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), direction,"createdAt");
        ApiResponse<Page<BookmarkResponse>> apiResponse = bookmarkService.getAllBookmarksByUser(pageable, authUser, request.getTarget());
        return ApiResponse.of(apiResponse);
    }

    @Operation(summary = "북마크 토글", description = "북마크 존재시 -> 삭제 / 없을시 -> 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> togleBookmark(
            @Valid @RequestBody BookmarkRequest.BookmarkRequestCreate reqDto,
            @AuthenticationPrincipal AuthUser authUser
    ){
        ApiResponse<Void> apiResponse = bookmarkService.togleBookmark(reqDto,authUser);
        return ApiResponse.of(apiResponse);
    }

    @Operation(summary = "북마크 상태 확인", description = "특정 대상이 북마크되어 있는지 확인하는 API")
    @GetMapping("/status")
    public boolean isBookmarked(
            @RequestParam Long targetId,
            @RequestParam BookmarkTargetType target,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        boolean bookmarked = bookmarkService.isBookmarked(targetId, target, authUser);
        System.out.println("bookmarked" + bookmarked);
        return bookmarked;
    }




}

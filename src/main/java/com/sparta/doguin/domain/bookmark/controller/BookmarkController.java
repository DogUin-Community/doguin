package com.sparta.doguin.domain.bookmark.controller;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.bookmark.model.BookmarkDto;
import com.sparta.doguin.domain.bookmark.service.BookmarkService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @GetMapping("/{bookmarkId}")
    public ResponseEntity<ApiResponse<BookmarkDto>> getBookmark(@PathVariable Long bookmarkId) {
        ApiResponse<BookmarkDto> apiResponse = bookmarkService.getBookmark(bookmarkId);
        return ApiResponse.of(apiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createBookmark(@RequestBody BookmarkDto.BookmarkRequest reqDto, @AuthenticationPrincipal AuthUser authUser){
        ApiResponse<Void> apiResponse = bookmarkService.createBookmark(reqDto,authUser);
        return ApiResponse.of(apiResponse);
    }

    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<ApiResponse<Void>> deleteBookmark(@PathVariable Long bookmarkId){
        ApiResponse<Void> apiResponse = bookmarkService.deleteBookmark(bookmarkId);
        return ApiResponse.of(apiResponse);
    }

}

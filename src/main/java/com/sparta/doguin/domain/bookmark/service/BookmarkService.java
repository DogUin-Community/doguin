package com.sparta.doguin.domain.bookmark.service;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.bookmark.model.BookmarkDto;
import com.sparta.doguin.domain.common.response.ApiResponse;

public interface BookmarkService {
    ApiResponse<BookmarkDto> getBookmark(Long bookmarkId);
    ApiResponse<Void> createBookmark(BookmarkDto.BookmarkRequest reqDto, AuthUser authUser);
    ApiResponse<Void> deleteBookmark(Long bookmarkId);
}

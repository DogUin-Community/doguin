package com.sparta.doguin.domain.bookmark.service;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import com.sparta.doguin.domain.bookmark.model.BookmarkDto;
import com.sparta.doguin.domain.common.response.ApiResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookmarkService {
    ApiResponse<BookmarkDto> getBookmark(Long bookmarkId);

    ApiResponse<Void> createBookmark(BookmarkDto.BookmarkRequest reqDto, AuthUser authUser);
    ApiResponse<Void> deleteBookmark(Long bookmarkId);
    ApiResponse<List<BookmarkDto>> getAllBookmarksByUser(Pageable pageable, AuthUser authUser, BookmarkTargetType target);
}

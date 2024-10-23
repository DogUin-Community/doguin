package com.sparta.doguin.domain.bookmark.service;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import com.sparta.doguin.domain.bookmark.model.BookmarkDto;
import com.sparta.doguin.domain.common.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookmarkService {
    ApiResponse<Void> createBookmark(BookmarkDto.BookmarkRequest reqDto, AuthUser authUser);
    ApiResponse<Void> deleteBookmark(Long bookmarkId,AuthUser authUser);
    ApiResponse<Page<BookmarkDto>> getAllBookmarksByUser(Pageable pageable, AuthUser authUser, BookmarkTargetType target);
}

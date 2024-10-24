package com.sparta.doguin.domain.bookmark.service;

import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import com.sparta.doguin.domain.bookmark.model.BookmarkRequest;
import com.sparta.doguin.domain.bookmark.model.BookmarkResponse;
import com.sparta.doguin.domain.common.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookmarkService {
    ApiResponse<Void> createBookmark(BookmarkRequest.BookmarkRequestCreate reqDto, AuthUser authUser);
    ApiResponse<Void> deleteBookmark(Long bookmarkId,AuthUser authUser);
    ApiResponse<Page<BookmarkResponse>> getAllBookmarksByUser(Pageable pageable, AuthUser authUser, BookmarkTargetType target);
}

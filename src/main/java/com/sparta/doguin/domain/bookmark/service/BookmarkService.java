package com.sparta.doguin.domain.bookmark.service;

import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import com.sparta.doguin.domain.bookmark.model.BookmarkRequest;
import com.sparta.doguin.domain.bookmark.model.BookmarkResponse;
import com.sparta.doguin.domain.common.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookmarkService {
    ApiResponse<Void> togleBookmark(BookmarkRequest.BookmarkRequestCreate reqDto, AuthUser authUser);
    ApiResponse<Page<BookmarkResponse>> getAllBookmarksByUser(Pageable pageable, AuthUser authUser, BookmarkTargetType target);
    boolean isBookmarked(Long targetId, BookmarkTargetType target, AuthUser authUser);
}

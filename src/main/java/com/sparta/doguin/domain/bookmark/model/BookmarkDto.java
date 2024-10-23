package com.sparta.doguin.domain.bookmark.model;

import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import com.sparta.doguin.domain.bookmark.entity.Bookmark;

public sealed interface BookmarkDto permits BookmarkDto.BookmarkResponse, BookmarkDto.BookmarkRequest {
    record BookmarkRequest(Long targetId, BookmarkTargetType target) implements BookmarkDto {

    }

    record BookmarkResponse(Long id, Long userId, Long targetId,BookmarkTargetType target) implements BookmarkDto {
        public static BookmarkResponse of(Bookmark bookmark) {
            return new BookmarkResponse(
                    bookmark.getId(),
                    bookmark.getUser().getId(),
                    bookmark.getTargetId(),
                    bookmark.getTarget()
            );
        }
    }
}

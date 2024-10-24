package com.sparta.doguin.domain.bookmark.model;

import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import com.sparta.doguin.domain.bookmark.entity.Bookmark;

import java.time.LocalDateTime;

public sealed interface BookmarkResponse permits BookmarkResponse.BookmarkResponseGet {

    record BookmarkResponseGet(
            Long id,
            Long userId,
            Long targetId,
            BookmarkTargetType target,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) implements BookmarkResponse {
        public static BookmarkResponse of(Bookmark bookmark) {
            return new BookmarkResponseGet(
                    bookmark.getId(),
                    bookmark.getUser().getId(),
                    bookmark.getTargetId(),
                    bookmark.getTarget(),
                    bookmark.getCreatedAt(),
                    bookmark.getUpdatedAt()
            );
        }
    }
}

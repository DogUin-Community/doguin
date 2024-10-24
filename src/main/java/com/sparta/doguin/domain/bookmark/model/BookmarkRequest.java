package com.sparta.doguin.domain.bookmark.model;

import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;

public sealed interface BookmarkRequest permits BookmarkRequest.BookmarkRequestCreate {
    record BookmarkRequestCreate(
            Long targetId,
            BookmarkTargetType target
    ) implements com.sparta.doguin.domain.bookmark.model.BookmarkRequest {

    }
}

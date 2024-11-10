package com.sparta.doguin.domain.bookmark.model;

import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public sealed interface BookmarkRequest permits BookmarkRequest.BookmarkRequestCreate {
    record BookmarkRequestCreate(
            @NotNull(message = "타겟 ID는 공백 일 수 없습니다") @Positive(message = "타겟 ID는 0을 초과 해야 합니다") Long targetId,
            @NotNull(message = "북마크 타겟타입은 공백 일 수 없습니다") BookmarkTargetType target
    ) implements BookmarkRequest {

    }
}

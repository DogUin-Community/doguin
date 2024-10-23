package com.sparta.doguin.domain.bookmark.validator;

import com.sparta.doguin.domain.common.exception.ValidatorException;

import static com.sparta.doguin.domain.common.response.ApiResponseBookmarkEnum.BOOKMARK_IS_NOT_ME;

public class BookmarkValidator {
    public static void isMe(Long id, Long targetId) {
        if (!id.equals(targetId) ) {
            throw new ValidatorException(BOOKMARK_IS_NOT_ME);
        }
    }
}

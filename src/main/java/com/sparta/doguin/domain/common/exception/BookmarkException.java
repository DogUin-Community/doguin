package com.sparta.doguin.domain.common.exception;


import com.sparta.doguin.domain.common.response.ApiResponseEnum;

public class BookmarkException extends BaseException {
    public BookmarkException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }
}

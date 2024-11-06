package com.sparta.doguin.domain.common.exception;

import com.sparta.doguin.domain.common.response.ApiResponseEnum;

public class DiscussionException extends BaseException {
    public DiscussionException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }
}

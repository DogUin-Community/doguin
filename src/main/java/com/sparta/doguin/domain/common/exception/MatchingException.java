package com.sparta.doguin.domain.common.exception;

import com.sparta.doguin.domain.common.response.ApiResponseEnum;

public class MatchingException extends BaseException {
    public MatchingException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }
}

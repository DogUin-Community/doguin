package com.sparta.doguin.domain.common.exception;

import com.sparta.doguin.domain.common.response.ApiResponseEnum;

public class QuestionException extends BaseException {
    public QuestionException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }
}

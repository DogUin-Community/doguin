package com.sparta.doguin.domain.common.exception;

import com.sparta.doguin.domain.common.response.ApiResponseEnum;

public class AnswerException extends BaseException {
    public AnswerException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }
}

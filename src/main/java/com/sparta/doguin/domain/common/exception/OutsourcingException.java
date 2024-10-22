package com.sparta.doguin.domain.common.exception;

import com.sparta.doguin.domain.common.response.ApiResponseEnum;

public class OutsourcingException extends BaseException {
    public OutsourcingException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }
}

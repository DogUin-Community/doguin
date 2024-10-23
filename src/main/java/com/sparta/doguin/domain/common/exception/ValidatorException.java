package com.sparta.doguin.domain.common.exception;


import com.sparta.doguin.domain.common.response.ApiResponseEnum;

public class ValidatorException extends BaseException {
    public ValidatorException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }
}

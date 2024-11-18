package com.sparta.doguin.domain.common.exception;


import com.sparta.doguin.domain.common.response.ApiResponseEnum;

public class InvalidRequestException extends BaseException {
    public InvalidRequestException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }
}

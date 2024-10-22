package com.sparta.doguin.domain.common.exception;

import com.sparta.doguin.domain.common.response.ApiResponseEnum;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ApiResponseEnum apiResponseEnum;

    public BaseException(ApiResponseEnum apiResponseEnum) {
        this.apiResponseEnum = apiResponseEnum;
    }
}
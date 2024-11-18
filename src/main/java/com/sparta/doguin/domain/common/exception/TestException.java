package com.sparta.doguin.domain.common.exception;

import com.sparta.doguin.domain.common.response.ApiResponseEnum;

public class TestException extends BaseException {
    public TestException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }
}

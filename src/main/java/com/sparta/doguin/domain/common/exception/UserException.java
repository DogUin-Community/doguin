package com.sparta.doguin.domain.common.exception;

import com.sparta.doguin.domain.common.response.ApiResponseEnum;

public class UserException extends BaseException {
    public UserException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }
}

package com.sparta.doguin.domain.common.exception;

import com.sparta.doguin.domain.common.response.ApiResponseEnum;

public class ChatException extends BaseException {
    public ChatException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }
}

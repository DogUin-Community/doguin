package com.sparta.doguin.domain.common.exception;


import com.sparta.doguin.domain.common.response.ApiResponseEnum;

public class FileException extends BaseException {
    public FileException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }
}

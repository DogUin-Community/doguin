package com.sparta.doguin.domain.common.exception;


import com.sparta.doguin.domain.common.response.ApiResponseEnum;

/**
 *  무언가 못찾을때 발생되는 예외
 */
public class HandleNotFound extends BaseException {
    public HandleNotFound(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }
}

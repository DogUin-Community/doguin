package com.sparta.doguin.domain.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseMypageEnum implements ApiResponseEnum{
    // 200 - OK
    MYPAGE_GET_SUCCESS(HttpStatus.OK, "마이페이지 조회에 성공하였습니다.");




    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseMypageEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = httpStatus.value();
    }
}
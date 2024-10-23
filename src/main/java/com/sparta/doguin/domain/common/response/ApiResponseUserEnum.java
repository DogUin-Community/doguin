package com.sparta.doguin.domain.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseUserEnum implements ApiResponseEnum {
    //200
    USER_CREATE_SUCCESS(HttpStatus.OK,"회원 가입에 성공하였습니다."),
    USER_LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공하였습니다.");







    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseUserEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}

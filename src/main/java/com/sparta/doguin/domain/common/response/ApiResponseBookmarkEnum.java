package com.sparta.doguin.domain.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseBookmarkEnum implements ApiResponseEnum {
    // 200
    BOOKMARK_OK(HttpStatus.OK,"북마크 작업에 성공 하였습니다"),


    // 404
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND ,"북마크가 존재 하지 않습니다")
    ;


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseBookmarkEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}

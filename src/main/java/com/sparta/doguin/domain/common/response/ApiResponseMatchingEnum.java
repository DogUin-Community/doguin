package com.sparta.doguin.domain.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseMatchingEnum implements ApiResponseEnum {
    // 200
    MATHCING_SUCCESS(HttpStatus.OK,"매칭작업에 성공 하였습니다"),

    // 403
    MATHCING_IS_NOT_ME(HttpStatus.FORBIDDEN, "자신이 신청한 매칭이 아니기에, 권한이 존재하지 않습니다"),

    // 404
    MATCHING_NOT_FOUND(HttpStatus.NOT_FOUND,"매칭 대상이 존재하지 않습니다")
    ;


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseMatchingEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}

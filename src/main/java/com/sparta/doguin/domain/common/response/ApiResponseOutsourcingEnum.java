package com.sparta.doguin.domain.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseOutsourcingEnum implements ApiResponseEnum {
    // 200
    OUTSOURCING_SUCCESS(HttpStatus.OK,"외주 작업 요청에 성공 하였습니다"),


    // 403
    OUTSOURCING_IS_NOT_ME(HttpStatus.FORBIDDEN, "자신이 작성한 외주가 아니기에, 권한이 존재하지 않습니다"),

    // 404
    OUTSOURCING_NOT_FOUND(HttpStatus.NOT_FOUND,"외주를 찾지 못하였습니다")
    ;


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseOutsourcingEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}

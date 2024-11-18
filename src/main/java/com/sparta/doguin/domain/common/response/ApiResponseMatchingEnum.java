package com.sparta.doguin.domain.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseMatchingEnum implements ApiResponseEnum {
    // 200
    MATHCING_SUCCESS(HttpStatus.OK,"매칭작업에 성공 하였습니다"),

    // 400
    MATCHING_IS_MANY(HttpStatus.BAD_REQUEST,"매칭 작업 요청이 너무 많습니다, 이후에 다시 시도해주세요"),

    // 403
    IS_NOT_ME(HttpStatus.FORBIDDEN, "자신이 작성하지않은 데이터가 포함되어 있습니다"),
    IS_NOT_COMPANY(HttpStatus.FORBIDDEN, "회사권한이 아닙니다"),
    IS_NOT_INDIVIDUAL(HttpStatus.FORBIDDEN, "개인권한이 아닙니다"),

    // 404
    MATCHING_NOT_FOUND(HttpStatus.NOT_FOUND,"매칭 대상이 존재하지 않습니다"),

    // 409
    MATCHING_LOCK(HttpStatus.BAD_REQUEST,"이미 다른 담당자가 처리한 사항입니다"),
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

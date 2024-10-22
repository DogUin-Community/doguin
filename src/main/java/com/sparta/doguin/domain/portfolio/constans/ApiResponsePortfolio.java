package com.sparta.doguin.domain.portfolio.constans;

import com.sparta.doguin.domain.common.response.ApiResponseEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponsePortfolio implements ApiResponseEnum {
    TEST_SUCCESS(HttpStatus.OK, "테스트 성공"),
    TEST_FAIL(HttpStatus.BAD_REQUEST, "테스트 실패")
    ;
    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponsePortfolio(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.code = httpStatus.value();
        this.message = message;
    }
}

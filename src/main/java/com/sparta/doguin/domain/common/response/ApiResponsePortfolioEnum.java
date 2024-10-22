package com.sparta.doguin.domain.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponsePortfolioEnum implements ApiResponseEnum {
    // 200
    PORTFOLIO_OK(HttpStatus.OK, "포트폴리오 작업 요청에 성공 하였습니다"),

    // 404
    PORTFOLIO_NOT_FOUND(HttpStatus.NOT_FOUND,"포트폴리오가 존재하지 않습니다")
    ;


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponsePortfolioEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.code = httpStatus.value();
        this.message = message;
    }
}

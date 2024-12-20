package com.sparta.doguin.domain.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseFileEnum implements ApiResponseEnum {

    // 200
    FILE_OK(HttpStatus.OK, "파일 작업요청에 성공 하였습니다"),

    // 400
    FILE_NOT_VALIDATE_EXTENSION( HttpStatus.BAD_REQUEST,"파일 확장자는 jpg, jpeg, png, gif, bmp, tiff, svg, webp, ico 만 가능합니다."),
    FILE_NOT_MATCH_SIZE( HttpStatus.BAD_REQUEST,"파일 사이즈는 2,000,000바이트 (2mb)를 넘길 수 없습니다"),

    // 403
    FILE_IS_NOT_ME(HttpStatus.FORBIDDEN,"파일 첨부자와, 현재 작업 하려는 요청자가 일치하지 않습니다"),

    // 404
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND,"파일이 존재하지 않습니다"),
    FILE_COUNT_NOT_MATCH(HttpStatus.NOT_FOUND,"일부 파일이 존재하지 않습니다"),

    // 500
    FILE_IO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 작업에 실패 하였습니다"),
    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseFileEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }

}

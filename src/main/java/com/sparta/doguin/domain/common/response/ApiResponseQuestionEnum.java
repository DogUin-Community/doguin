package com.sparta.doguin.domain.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseQuestionEnum implements ApiResponseEnum {

    QUESTION_CREATE_SUCCESS(HttpStatus.OK, "질문 등록에 성공하였습니다."),
    QUESTION_UPDATE_SUCCESS(HttpStatus.OK, "질문 수정에 성공하셨습니다."),
    QUESTION_DELETE_SUCCESS(HttpStatus.OK, "질문 삭제에 성공하였습니다."),
    QUESTION_FIND_ALL_SUCCESS(HttpStatus.OK, "질문 조회(전체)에 성공하였습니다."),
    QUESTION_FIND_ONE_SUCCESS(HttpStatus.OK, "질문 조회(단건)에 성공하였습니다."),

    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "질문을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseQuestionEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}

package com.sparta.doguin.domain.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseBoardEnum implements ApiResponseEnum {
    // 200
    NOTICE_CREATE_SUCCESS(HttpStatus.OK,"공지 등록에 성공하였습니다."),
    NOTICE_UPDATE_SUCCESS(HttpStatus.OK,"공지 수정에 성공하였습니다."),
    NOTICE_DELETE_SUCCESS(HttpStatus.OK,"공지 삭제에 성공하였습니다."),
    NOTICE_FIND_ONE_SUCCESS(HttpStatus.OK,"공지 조회(단건)에 성공하였습니다."),
    NOTICE_FIND_ALL_SUCCESS(HttpStatus.OK,"공지 조회(전체)에 성공하였습니다."),
    NOTICE_SEARCH_SUCCESS(HttpStatus.OK,"공지 검색에 성공하였습니다."),
    // 400



    // 403


    // 404
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "공지를 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseBoardEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}

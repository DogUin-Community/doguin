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

    BULLETIN_CREATE_SUCCESS(HttpStatus.OK,"일반 게시글 등록에 성공하였습니다."),
    BULLETIN_UPDATE_SUCCESS(HttpStatus.OK,"일반 게시글 수정에 성공하였습니다."),
    BULLETIN_DELETE_SUCCESS(HttpStatus.OK,"일반 게시글 삭제에 성공하였습니다."),
    BULLETIN_FIND_ONE_SUCCESS(HttpStatus.OK,"일반 게시글 조회(단건)에 성공하였습니다."),
    BULLETIN_FIND_ALL_SUCCESS(HttpStatus.OK,"일반 게시글 조회(전체)에 성공하였습니다."),
    BULLETIN_POPULAR__FIND_ALL_SUCCESS(HttpStatus.OK,"일반 게시글 조회(인기, 전체)에 성공하였습니다."),
    BULLETIN_SEARCH_SUCCESS(HttpStatus.OK,"일반 게시글 검색에 성공하였습니다."),

    EVENT_CREATE_SUCCESS(HttpStatus.OK,"이벤트 등록에 성공하였습니다."),
    EVENT_UPDATE_SUCCESS(HttpStatus.OK,"이벤트 수정에 성공하였습니다."),
    EVENT_DELETE_SUCCESS(HttpStatus.OK,"이벤트 삭제에 성공하였습니다."),
    EVENT_FIND_ONE_SUCCESS(HttpStatus.OK,"이벤트 조회(단건)에 성공하였습니다."),
    EVENT_FIND_ALL_SUCCESS(HttpStatus.OK,"이벤트 조회(전체)에 성공하였습니다."),
    EVENT_SEARCH_SUCCESS(HttpStatus.OK,"이벤트 검색에 성공하였습니다."),

    INQUIRY_CREATE_SUCCESS(HttpStatus.OK,"문의 등록에 성공하였습니다."),
    INQUIRY_UPDATE_SUCCESS(HttpStatus.OK,"문의 수정에 성공하였습니다."),
    INQUIRY_DELETE_SUCCESS(HttpStatus.OK,"문의 삭제에 성공하였습니다."),
    INQUIRY_FIND_ONE_SUCCESS(HttpStatus.OK,"문의 조회(단건)에 성공하였습니다."),
    INQUIRY_FIND_ALL_SUCCESS(HttpStatus.OK,"문의 조회(전체)에 성공하였습니다."),
    INQUIRY_SEARCH_SUCCESS(HttpStatus.OK,"문의 검색에 성공하였습니다."),

    // 400
    NOTICE_WRONG(HttpStatus.BAD_REQUEST,"공지만 접근이 가능합니다."),
    BULLETIN_WRONG(HttpStatus.BAD_REQUEST,"일반 게시물만 접근이 가능합니다."),
    EVENT_WRONG(HttpStatus.BAD_REQUEST,"이벤트만 접근이 가능합니다."),
    INQUIRY_WRONG(HttpStatus.BAD_REQUEST,"문의만 접근이 가능합니다."),
    USER_WRONG(HttpStatus.BAD_REQUEST,"작성자가 아닙니다."),


    // 403


    // 404
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "공지를 찾을 수 없습니다."),
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "이벤트를 찾을 수 없습니다."),
    INQUIRY_NOT_FOUND(HttpStatus.NOT_FOUND, "문의를 찾을 수 없습니다."),
    BULLETIN_NOT_FOUND(HttpStatus.NOT_FOUND, "일반 게시글을 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseBoardEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}

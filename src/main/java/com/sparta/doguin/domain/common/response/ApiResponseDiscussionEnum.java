package com.sparta.doguin.domain.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseDiscussionEnum implements ApiResponseEnum {
    // 200 - 성공 응답
    DISCUSSION_CREATE_SUCCESS(HttpStatus.OK, "토론이 성공적으로 생성되었습니다."),
    DISCUSSION_UPDATE_SUCCESS(HttpStatus.OK, "토론이 성공적으로 수정되었습니다."),
    DISCUSSION_DELETE_SUCCESS(HttpStatus.OK, "토론이 성공적으로 삭제되었습니다."),
    DISCUSSION_FETCH_SUCCESS(HttpStatus.OK, "토론을 성공적으로 조회했습니다."),
    DISCUSSION_LIST_FETCH_SUCCESS(HttpStatus.OK, "토론 목록을 성공적으로 조회했습니다."),
    REPLY_CREATE_SUCCESS(HttpStatus.OK, "답변이 성공적으로 추가되었습니다."),
    REPLY_DELETE_SUCCESS(HttpStatus.OK, "답변이 성공적으로 삭제되었습니다."),
    REPLY_UPDATE_SUCCESS(HttpStatus.OK, "답변이 성공적으로 수정되었습니다."),
    ATTACHMENT_UPDATE_SUCCESS(HttpStatus.OK, "첨부파일이 성공적으로 수정되었습니다."),
    ATTACHMENT_DELETE_SUCCESS(HttpStatus.OK, "첨부파일이 성공적으로 삭제되었습니다."),

    // 400 - 잘못된 요청
    INVALID_DISCUSSION_TITLE(HttpStatus.BAD_REQUEST, "유효하지 않은 토론 제목입니다."),
    INVALID_DISCUSSION_CONTENT(HttpStatus.BAD_REQUEST, "유효하지 않은 토론 내용입니다."),
    INVALID_REPLY_CONTENT(HttpStatus.BAD_REQUEST, "유효하지 않은 답변 내용입니다."),
    DISCUSSION_ALREADY_CLOSED(HttpStatus.BAD_REQUEST, "이 토론은 종료되어 답변을 달 수 없습니다."),

    // 403 - 권한 오류
    UNAUTHORIZED_USER(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다."),
    NOT_DISCUSSION_OWNER(HttpStatus.FORBIDDEN, "토론을 작성한 사용자만 수정 및 삭제할 수 있습니다."),
    NOT_REPLY_OWNER(HttpStatus.FORBIDDEN, "답변을 작성한 사용자만 삭제할 수 있습니다."),

    // 404 - 리소스 없음
    DISCUSSION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 토론을 찾을 수 없습니다."),
    REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 답변을 찾을 수 없습니다."),

    // 500 - 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseDiscussionEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = httpStatus.value();
    }
}

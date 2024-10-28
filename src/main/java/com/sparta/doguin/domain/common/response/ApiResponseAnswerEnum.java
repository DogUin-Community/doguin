package com.sparta.doguin.domain.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseAnswerEnum implements ApiResponseEnum {

    // 200
    QUESTION_ANSWER_CREATE_SUCCESS(HttpStatus.OK, "질문 답변 등록에 성공하였습니다."),
    QUESTION_ANSWER_UPDATE_SUCCESS(HttpStatus.OK, "질문 답변 수정에 성공하셨습니다."),
    QUESTION_ANSWER_DELETE_SUCCESS(HttpStatus.OK, "질문 답변 삭제에 성공하였습니다."),
    QUESTION_ANSWER_FIND_ALL_SUCCESS(HttpStatus.OK, "질문 답변 조회(전체)에 성공하였습니다."),
    QUESTION_ANSWER_FIND_ONE_SUCCESS(HttpStatus.OK, "질문 답변 조회(단건)에 성공하였습니다."),

    APPLY_ANSWER_CREATE_SUCCESS(HttpStatus.OK, "질문 대답변 등록에 성공하였습니다."),
    APPLY_ANSWER_UPDATE_SUCCESS(HttpStatus.OK, "질문 대답변 수정에 성공하셨습니다."),
    APPLY_ANSWER_DELETE_SUCCESS(HttpStatus.OK, "질문 대답변 삭제에 성공하였습니다."),
    APPLY_ANSWER_FIND_ALL_SUCCESS(HttpStatus.OK, "질문 대답변 조회(전체)에 성공하였습니다."),
    APPLY_ANSWER_FIND_ONE_SUCCESS(HttpStatus.OK, "질문 대답변 조회(단건)에 성공하였습니다."),

    // 자유 게시판, 공지사항 댓글
    COMMENT_ANSWER_CREATE_SUCCESS(HttpStatus.OK, "댓글 등록에 성공하였습니다."),
    COMMENT_ANSWER_UPDATE_SUCCESS(HttpStatus.OK, "댓글 수정에 성공하셨습니다."),
    COMMENT_ANSWER_DELETE_SUCCESS(HttpStatus.OK, "댓글 삭제에 성공하였습니다."),
    COMMENT_ANSWER_FIND_ALL_SUCCESS(HttpStatus.OK, "댓글 조회(전체)에 성공하였습니다."),
    COMMENT_ANSWER_FIND_ONE_SUCCESS(HttpStatus.OK, "댓글 조회(단건)에 성공하였습니다."),

    INQUIRY_ANSWER_CREATE_SUCCESS(HttpStatus.OK, "문의 답변 등록에 성공하였습니다."),
    INQUIRY_ANSWER_UPDATE_SUCCESS(HttpStatus.OK, "문의 답변 수정에 성공하셨습니다."),
    INQUIRY_ANSWER_DELETE_SUCCESS(HttpStatus.OK, "문의 답변 삭제에 성공하였습니다."),
    INQUIRY_ANSWER_FIND_ALL_SUCCESS(HttpStatus.OK, "문의 답변 조회(전체)에 성공하였습니다."),
    INQUIRY_ANSWER_FIND_ONE_SUCCESS(HttpStatus.OK, "문의 답변 조회(단건)에 성공하였습니다."),

    // 404
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "질문을 찾을 수 없습니다."),
    QUESTION_ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "질문 답변을 찾을 수 없습니다."),
    APPLY_ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "질문 대답변을 찾을 수 없습니다."),
    COMMENT_ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    INQUIRY_ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "문의 답변을 찾을 수 없습니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),

    // 400
    ANSWER_BELONG_TO_QUESTION(HttpStatus.BAD_REQUEST, "질문에 속해 있는 답변이 아닙니다."),

    // 401
    UPDATE_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "본인이 작성한 글만 수정이 가능합니다."),
    DELETE_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "본인이 작성한 글만 삭제가 가능합니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseAnswerEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }

}

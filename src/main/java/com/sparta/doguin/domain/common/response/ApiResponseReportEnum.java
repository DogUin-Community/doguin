package com.sparta.doguin.domain.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseReportEnum implements ApiResponseEnum {
    // 200
    REPORT_RECEIVED_SUCCESS(HttpStatus.OK,"신고가 접수되었습니다."),
    REPORT_ACCEPTED_SUCCESS(HttpStatus.OK,"신고가 승인되었습니다."),
    REPORT_REJECTED_SUCCESS(HttpStatus.OK,"신고가 반려되었습니다."),
    REPORT_VIEW_SUCCESS(HttpStatus.OK,"신고 조회가 완료되었습니다."),



    // 400
    NOTICE_WRONG(HttpStatus.BAD_REQUEST,"공지만 접근이 가능합니다."),



    // 403
    REPORT_TYPE_WRONG(HttpStatus.FORBIDDEN, "이미 처리된 신고 입니다."),
    REPORT_ALREADY_EXIST(HttpStatus.FORBIDDEN, "이미 신고 하셨습니다."),

    // 404

    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND,"신고를 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseReportEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}

package com.sparta.doguin.domain.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseFollowEnum implements ApiResponseEnum {
    // 200 - OK
    FOLLOW_SUCCESS(HttpStatus.OK, "팔로우에 성공하였습니다."),
    FOLLOW_GET_SUCCESS(HttpStatus.OK, "팔로우 조회에 성공하였습니다."),
    UNFOLLOW_SUCCESS(HttpStatus.OK, "언팔로우에 성공하였습니다."),

    // 400 - BAD_REQUEST
    ALREADY_FOLLOWING(HttpStatus.BAD_REQUEST, "이미 팔로우 중입니다."),

    // 404 - NOT_FOUND
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "팔로우 관계를 찾지 못하였습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;




    ApiResponseFollowEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = httpStatus.value();
    }
}

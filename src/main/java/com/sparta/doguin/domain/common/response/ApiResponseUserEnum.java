package com.sparta.doguin.domain.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseUserEnum implements ApiResponseEnum {
    // 200 - OK
    USER_CREATE_SUCCESS(HttpStatus.OK,"회원 가입에 성공하였습니다."),
    USER_LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공하였습니다."),
    USER_SOCIALOGIN_SUCCESS(HttpStatus.OK, "소셜로그인에 성공하였습니다."),
    USER_CHECK_SUCCESS(HttpStatus.OK, "회원 정보 조회에 성공하였습니다."),
    USER_UPDATE_SUCCESS(HttpStatus.OK, "회원 정보 수정에 성공하였습니다."),
    USER_DELETE_SUCCESS(HttpStatus.OK, "회원 탈퇴에 성공하였습니다."),

    // 202 - Accepted
    USER_VERIFICATION_REQUIRED(HttpStatus.ACCEPTED, "본인 확인이 필요합니다."),

    // 400 - BAD_REQUEST
    USER_ROLE_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 UserRole입니다."),
    USER_TYPE_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 UserType입니다."),
    USER_GRADE_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 UserGrade입니다."),
    USER_PROVIDER_TYPE_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 ProviderType입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다 !!!"),
    PASSWORD_REQUIRED(HttpStatus.BAD_REQUEST, "비밀번호가 필요합니다. 기존 계정과 연동하려면 비밀번호를 입력하세요."),
    INVALID_SOCIAL_PROVIDER(HttpStatus.BAD_REQUEST, "제공하지 않는 소셜 로그인 방식입니다."),
    FAILED_TO_FETCH_SOCIAL_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "소셜 로그인 액세스 토큰을 가져오는데 실패했습니다."),
    FAILED_TO_FETCH_SOCIAL_USER_INFO(HttpStatus.BAD_REQUEST, "소셜 로그인 사용자 정보를 가져오는데 실패했습니다."),

    // 404 - NOT_FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾지 못하였습니다."),

    // 409 - CONFLICT
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    USER_NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다.");




    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseUserEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}

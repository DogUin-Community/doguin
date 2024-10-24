package com.sparta.doguin.domain.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseChatEnum implements ApiResponseEnum{
    // 200
    CHATROOM_CREATE_SUCCESS(HttpStatus.OK, "채팅방이 성공적으로 생성되었습니다."),
    MESSAGE_SEND_SUCCESS(HttpStatus.OK, "메시지가 성공적으로 전송되었습니다."),
    CHATROOM_ENTER_SUCCESS(HttpStatus.OK, "채팅방에 성공적으로 입장했습니다."),
    CHATROOM_LEAVE_SUCCESS(HttpStatus.OK, "채팅방에서 성공적으로 퇴장했습니다."),

    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."),
    CHATROOM_FULL(HttpStatus.FORBIDDEN, "채팅방 인원이 가득 찼습니다."),
    MESSAGE_SEND_FAILURE(HttpStatus.BAD_REQUEST, "메시지 전송에 실패하였습니다.");


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseChatEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}

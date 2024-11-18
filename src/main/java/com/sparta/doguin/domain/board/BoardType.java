package com.sparta.doguin.domain.board;

public enum BoardType {

    BOARD_BULLETIN("Bulletin"), // 일반 게시물
    BOARD_NOTICE("Notice"), // 공지 게시물
    BOARD_EVENT("Event"), // 이벤트 게시물
    BOARD_INQUIRY("Inquiry"); // 문의 게시물

    private final String type;

    BoardType(String type) {
        this.type = type;
    }
}

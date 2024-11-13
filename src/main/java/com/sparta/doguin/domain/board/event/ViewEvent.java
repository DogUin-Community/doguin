package com.sparta.doguin.domain.board.event;

import lombok.Getter;

@Getter
public class ViewEvent {

    private final Long boardId;
    private final Long userId;

    public ViewEvent(Long boardId, Long userId) {
        this.boardId = boardId;
        this.userId = userId;
    }

}

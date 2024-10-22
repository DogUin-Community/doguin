package com.sparta.doguin.domain.board.dto.response;

import com.sparta.doguin.domain.board.entity.Board;

public record BoardResponse(
        Long id,
        String title,
        String content
) {
    public static BoardResponse from(Board board){
        return new BoardResponse(board.getId(),board.getTitle(),board.getContent());
    }
}

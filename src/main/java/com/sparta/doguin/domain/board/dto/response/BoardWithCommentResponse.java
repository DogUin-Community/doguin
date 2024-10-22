package com.sparta.doguin.domain.board.dto.response;

import com.sparta.doguin.domain.board.entity.Board;

public record BoardWithCommentResponse(
        Long id,
        String title,
        String content
) {
    public static BoardWithCommentResponse from(Board board){
        return new BoardWithCommentResponse(board.getId(),board.getTitle(),board.getContent());
    }
}

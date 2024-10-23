package com.sparta.doguin.domain.board.dto.response;

import com.sparta.doguin.domain.board.entity.Board;


public sealed interface BoardResponse permits BoardResponse.BoardCommonResponse, BoardResponse.BoardWithCommentResponse {

    record BoardCommonResponse(Long id,
                               String title,
                               String content) implements BoardResponse {
        public static BoardCommonResponse from(Board board) {
            return new BoardCommonResponse(board.getId(), board.getTitle(), board.getContent());
        }
    }

    record BoardWithCommentResponse(
            Long id,
            String title,
            String content)implements BoardResponse{
        public static BoardWithCommentResponse from(Board board) {
            return new BoardWithCommentResponse(board.getId(), board.getTitle(), board.getContent());
        }
    }
}
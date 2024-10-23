package com.sparta.doguin.domain.board.dto;

public sealed interface BoardResponse permits BoardResponse.BoardCommonResponse {

    record BoardCommonResponse(
            Long id,
            String title,
            String content) implements BoardResponse {
    }


}
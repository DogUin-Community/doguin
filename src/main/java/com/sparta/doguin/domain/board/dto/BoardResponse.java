package com.sparta.doguin.domain.board.dto;

import com.sparta.doguin.domain.board.entity.Board;

public sealed interface BoardResponse permits BoardResponse.BoardCommonResponse {

    record BoardCommonResponse(
            Long id,
            String title,
            String content) implements BoardResponse {

    }


}
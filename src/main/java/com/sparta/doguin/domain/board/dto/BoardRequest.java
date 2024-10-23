package com.sparta.doguin.domain.board.dto;

public sealed interface BoardRequest permits BoardRequest.BoardCommonRequest {

    record BoardCommonRequest(
            String title,
            String content) implements BoardRequest {
    }
}
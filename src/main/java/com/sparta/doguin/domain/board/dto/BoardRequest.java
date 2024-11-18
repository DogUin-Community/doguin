package com.sparta.doguin.domain.board.dto;


import java.util.List;

public sealed interface BoardRequest permits BoardRequest.BoardCommonRequest {

    record BoardCommonRequest(
            String title,
            String content,
            List<Long> fileIds
    ) implements BoardRequest {
    }

}
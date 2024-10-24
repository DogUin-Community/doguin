package com.sparta.doguin.domain.board.dto;

import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.board.entity.Board;
import org.springframework.data.domain.Page;

public sealed interface BoardResponse permits BoardResponse.BoardCommonResponse, BoardResponse.BoardWithAnswer {

    record BoardCommonResponse(
            Long id,
            String title,
            String content) implements BoardResponse {
    }

    record BoardWithAnswer(
            Long id,
            String title,
            String content,
            Page<AnswerResponse.Response> response
    )implements  BoardResponse{}


}
package com.sparta.doguin.domain.board.dto;

import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public sealed interface BoardResponse permits BoardResponse.BoardCommonResponse, BoardResponse.BoardWithAnswer {

    record BoardCommonResponse(
            Long id,
            String title

            ) implements BoardResponse, Serializable {
    }

    record BoardWithAnswer(
            Long id,
            String title,
            String content,
            Long view,
            List<String> filePath,
            Page<AnswerResponse.Response> response
    )implements  BoardResponse, Serializable {}


}
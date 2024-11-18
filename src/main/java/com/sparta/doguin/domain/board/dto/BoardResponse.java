package com.sparta.doguin.domain.board.dto;

import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public sealed interface BoardResponse permits BoardResponse.BoardCommonResponse, BoardResponse.BoardWithAnswer, BoardResponse.BoardWithAnswerWithUserId {

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
            Page<AnswerResponse.Response> response,
            List<String> filePaths
    )implements  BoardResponse, Serializable {}

    // 김경민이 추가함
    record BoardWithAnswerWithUserId(
            Long id,
            Long userId,
            String title,
            String content,
            Long view,
            Page<AnswerResponse.Response> response,
            List<String> filePaths
    )implements  BoardResponse, Serializable {}


}
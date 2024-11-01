package com.sparta.doguin.domain.answer.dto;

import java.io.Serializable;
import java.util.List;

public sealed interface AnswerResponse permits AnswerResponse.Response, AnswerResponse.GetResponse {
    record Response(Long id, String content) implements AnswerResponse {}

    record GetResponse(Long id, String content, List<GetResponse> apply) implements AnswerResponse, Serializable {}
}

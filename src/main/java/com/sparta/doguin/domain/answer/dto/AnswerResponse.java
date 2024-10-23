package com.sparta.doguin.domain.answer.dto;

public sealed interface AnswerResponse permits AnswerResponse.Response {

    record Response(Long id,
                  String content) implements AnswerResponse {}
}

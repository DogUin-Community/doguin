package com.sparta.doguin.domain.answer.dto;

public sealed interface AnswerRequest permits AnswerRequest.Request {

    record Request(String content) implements AnswerRequest {}
}

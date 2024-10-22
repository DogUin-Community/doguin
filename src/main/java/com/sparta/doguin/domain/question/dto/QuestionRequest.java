package com.sparta.doguin.domain.question.dto;

import com.sparta.doguin.domain.question.enums.FirstCategory;
import com.sparta.doguin.domain.question.enums.LastCategory;
import com.sparta.doguin.domain.question.enums.SecondCategory;

public sealed interface QuestionRequest permits QuestionRequest.CreatedQuestion, QuestionRequest.UpdateQuestion {

    record CreatedQuestion(String title,
                           String content,
                           FirstCategory firstCategory,
                           SecondCategory secondCategory,
                           LastCategory lastCategory) implements QuestionRequest {}

    record UpdateQuestion(String title,
                           String content,
                           FirstCategory firstCategory,
                           SecondCategory secondCategory,
                           LastCategory lastCategory) implements QuestionRequest {}
}

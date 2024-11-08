package com.sparta.doguin.domain.question.dto;

import com.sparta.doguin.domain.question.enums.FirstCategory;
import com.sparta.doguin.domain.question.enums.LastCategory;
import com.sparta.doguin.domain.question.enums.SecondCategory;

import java.util.List;

public sealed interface QuestionRequest permits QuestionRequest.QuestionRequestCreate, QuestionRequest.QuestionRequestUpdate {

    record QuestionRequestCreate(String title,
                                 String content,
                                 FirstCategory firstCategory,
                                 SecondCategory secondCategory,
                                 LastCategory lastCategory) implements QuestionRequest {}

    record QuestionRequestUpdate(String title,
                                 String content,
                                 FirstCategory firstCategory,
                                 SecondCategory secondCategory,
                                 LastCategory lastCategory,
                                 List<Long> files) implements QuestionRequest {}

}

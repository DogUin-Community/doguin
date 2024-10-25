package com.sparta.doguin.domain.question.dto;

import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.question.enums.FirstCategory;
import com.sparta.doguin.domain.question.enums.LastCategory;
import com.sparta.doguin.domain.question.enums.QuestionStatus;
import com.sparta.doguin.domain.question.enums.SecondCategory;
import org.springframework.data.domain.Page;

public sealed interface QuestionResponse permits QuestionResponse.CreatedQuestion, QuestionResponse.GetQuestions, QuestionResponse.GetQuestion {

    record CreatedQuestion(Long questionId,
                           String title,
                           String content,
                           FirstCategory firstCategory,
                           SecondCategory secondCategory,
                           LastCategory lastCategory,
                           QuestionStatus questionStatus) implements QuestionResponse {}

    record GetQuestions(Long questionId,
                        String title,
                        String content,
                        FirstCategory firstCategory,
                        SecondCategory secondCategory,
                        LastCategory lastCategory,
                        QuestionStatus questionStatus) implements QuestionResponse {}

    record GetQuestion(Long questionId,
                       String title,
                       String content,
                       FirstCategory firstCategory,
                       SecondCategory secondCategory,
                       LastCategory lastCategory,
                       QuestionStatus questionStatus,
                       Page<AnswerResponse.GetResponse> comment) implements QuestionResponse {}
}

package com.sparta.doguin.domain.question.dto;

import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.question.entity.Question;
import com.sparta.doguin.domain.question.enums.FirstCategory;
import com.sparta.doguin.domain.question.enums.LastCategory;
import com.sparta.doguin.domain.question.enums.QuestionStatus;
import com.sparta.doguin.domain.question.enums.SecondCategory;

import java.io.Serializable;
import java.util.List;

public sealed interface QuestionResponse permits QuestionResponse.QuestionResponseGetWithFiles, QuestionResponse.QuestionResponseGet, QuestionResponse.CreatedQuestion, QuestionResponse.GetQuestions, QuestionResponse.GetQuestion, QuestionResponse.SearchQuestion {

    record QuestionResponseGet (Long questionId,
                                String title,
                                String content,
                                FirstCategory firstCategory,
                                SecondCategory secondCategory,
                                LastCategory lastCategory,
                                QuestionStatus questionStatus) implements QuestionResponse, Serializable {
        public static QuestionResponseGet of(Question question) {
            return new QuestionResponseGet(
                    question.getId(),
                    question.getTitle(),
                    question.getContent(),
                    question.getFirstCategory(),
                    question.getSecondCategory(),
                    question.getLastCategory(),
                    question.getQuestionStatus()
            );
        }
    }

    record QuestionResponseGetWithFiles (Long questionId,
                                String title,
                                String content,
                                FirstCategory firstCategory,
                                SecondCategory secondCategory,
                                LastCategory lastCategory,
                                QuestionStatus questionStatus,
                                List<Long> files) implements QuestionResponse, Serializable {
        public static QuestionResponseGetWithFiles of(Question question, List<Long> files) {
            return new QuestionResponseGetWithFiles(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                question.getFirstCategory(),
                question.getSecondCategory(),
                question.getLastCategory(),
                question.getQuestionStatus(),
                files
            );
        }
    }

    record GetQuestions(Long questionId,
                        String title,
                        String content,
                        FirstCategory firstCategory,
                        SecondCategory secondCategory,
                        LastCategory lastCategory,
                        QuestionStatus questionStatus) implements QuestionResponse, Serializable {}

    record GetQuestion(Long questionId,
                       String title,
                       String content,
                       FirstCategory firstCategory,
                       SecondCategory secondCategory,
                       LastCategory lastCategory,
                       QuestionStatus questionStatus,
                       Long view,
                       List<AnswerResponse.GetResponse> answer,
                       List<Long> files) implements QuestionResponse, Serializable {}









    record CreatedQuestion(Long questionId,
                           String title,
                           String content,
                           FirstCategory firstCategory,
                           SecondCategory secondCategory,
                           LastCategory lastCategory,
                           QuestionStatus questionStatus) implements QuestionResponse, Serializable {}





    record SearchQuestion(Long questionId,
                          String title,
                          String content) implements QuestionResponse, Serializable {

        // Question 객체를 받아 SearchQuestion 객체를 생성하는 정적 메서드
        public static SearchQuestion of(Question question) {
            return new SearchQuestion(
                    question.getId(),
                    question.getTitle(),
                    question.getContent()
            );
        }
    }
}

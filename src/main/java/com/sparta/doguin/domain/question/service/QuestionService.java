package com.sparta.doguin.domain.question.service;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseTest;
import com.sparta.doguin.domain.question.dto.QuestionRequest;
import com.sparta.doguin.domain.question.dto.QuestionResponse;
import com.sparta.doguin.domain.question.entity.Question;
import com.sparta.doguin.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    /**
     * 질문 생성
     *
     * @param request
     * @return
     */
    public ApiResponse<QuestionResponse.CreatedQuestion> createdQuestion(QuestionRequest.CreatedQuestion request) {

        Question newQuestion = new Question(
            request.title(),
            request.content(),
            request.firstCategory(),
            request.secondCategory(),
            request.lastCategory()
        );

        questionRepository.save(newQuestion);

        return ApiResponse.of(ApiResponseTest.TEST_SUCCESS, new QuestionResponse.CreatedQuestion(newQuestion.getId(), newQuestion.getTitle(), newQuestion.getContent(), newQuestion.getFirstCategory(), newQuestion.getSecondCategory(), newQuestion.getLastCategory(), newQuestion.getQuestionStatus()));
    }

    /**
     * 질문 수정
     *
     * @param questionId
     * @param request
     * @return
     */
    public ApiResponse<QuestionResponse.CreatedQuestion> updatedQuestion(long questionId, QuestionRequest.UpdateQuestion request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 질문이 존재하지 않습니다."));

        question.update(request);
        questionRepository.save(question);

        return ApiResponse.of(ApiResponseTest.TEST_SUCCESS, new QuestionResponse.CreatedQuestion(question.getId(), question.getTitle(), question.getContent(), question.getFirstCategory(), question.getSecondCategory(), question.getLastCategory(), question.getQuestionStatus()));
    }

    /**
     * 보드 다건 조회
     * @return
     */
    public ApiResponse<List<QuestionResponse.GetQuestions>> getQuestions() {
        List<Question> questions = questionRepository.findAll();

        List<QuestionResponse.GetQuestions> response = questions.stream()
                .map(question -> new QuestionResponse.GetQuestions(question.getId(),
                                                                question.getTitle(),
                                                                question.getContent(),
                                                                question.getFirstCategory(),
                                                                question.getSecondCategory(),
                                                                question.getLastCategory(),
                                                                question.getQuestionStatus())).toList();

        return ApiResponse.of(ApiResponseTest.TEST_SUCCESS, response);
    }

    /**
     * 보드 단건 조회
     * @param questionId
     * @return
     */
    public ApiResponse<QuestionResponse.GetQuestion> getQuestion(long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 질문이 존재하지 않습니다."));

        return ApiResponse.of(ApiResponseTest.TEST_SUCCESS, new QuestionResponse.GetQuestion(question.getId(),
                                                                                            question.getTitle(),
                                                                                            question.getContent(),
                                                                                            question.getFirstCategory(),
                                                                                            question.getSecondCategory(),
                                                                                            question.getLastCategory(),
                                                                                            question.getQuestionStatus()));
    }

    /**
     * 보드 삭제
     * @param questionId
     * @return
     */
    public ApiResponse<Void> deleteQuestion(long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 질문이 존재하지 않습니다."));

        questionRepository.delete(question);

        return ApiResponse.of(ApiResponseTest.TEST_SUCCESS);
    }
}

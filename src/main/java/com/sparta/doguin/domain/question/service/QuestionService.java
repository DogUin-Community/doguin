package com.sparta.doguin.domain.question.service;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.common.exception.HandleNotFound;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseQuestionEnum;
import com.sparta.doguin.domain.question.dto.QuestionRequest;
import com.sparta.doguin.domain.question.dto.QuestionResponse;
import com.sparta.doguin.domain.question.entity.Question;
import com.sparta.doguin.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    /**
     * 질문 생성
     *
     * @param request 질문 생성 시 필요한 정보가 담긴 객체
     * @since 1.0
     * @return 생성된 질문의 정보를 포함하는 ApiResponse 객체
     * @author 유태이
     */
    public ApiResponse<QuestionResponse.CreatedQuestion> createdQuestion(QuestionRequest.CreatedQuestion request) {

        Question newQuestion = new Question(request.title(),
                                            request.content(),
                                            request.firstCategory(),
                                            request.secondCategory(),
                                            request.lastCategory());
        questionRepository.save(newQuestion);

        return ApiResponse.of(ApiResponseQuestionEnum.QUESTION_CREATE_SUCCESS, new QuestionResponse.CreatedQuestion(newQuestion.getId(), newQuestion.getTitle(), newQuestion.getContent(), newQuestion.getFirstCategory(), newQuestion.getSecondCategory(), newQuestion.getLastCategory(), newQuestion.getQuestionStatus()));
    }

    /**
     * 질문 수정
     *
     * @param questionId 수정할 질문의 ID
     * @param request 수정할 질문의 정보가 담긴 객체
     * @since 1.0
     * @throws HandleNotFound 질문 수정 시 데이터가 없을 경우 발생
     * @return 수정 된 질문의 정보를 포함하는 ApiResponse 객체
     * @author 유태이
     */
    public ApiResponse<QuestionResponse.CreatedQuestion> updatedQuestion(long questionId, QuestionRequest.UpdateQuestion request) {
        Question question = findById(questionId);
        question.update(request);
        questionRepository.save(question);
        return ApiResponse.of(ApiResponseQuestionEnum.QUESTION_UPDATE_SUCCESS, new QuestionResponse.CreatedQuestion(question.getId(), question.getTitle(), question.getContent(), question.getFirstCategory(), question.getSecondCategory(), question.getLastCategory(), question.getQuestionStatus()));
    }

    /**
     * 질문 다건 조회
     *
     * @param page 조회할 페이지 번호(기본 값: 1)
     * @param size 한 페이지에 포함될 질문 수(기본값: 10)
     * @since 1.0
     * @return 요청한 페이지에 해당하는 질문 목록
     * @author 유태이
     */
    public ApiResponse<Page<QuestionResponse.GetQuestions>> getQuestions(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Question> questions = questionRepository.findAll(pageable);

        Page<QuestionResponse.GetQuestions> response = questions
                .map(question -> new QuestionResponse.GetQuestions(question.getId(),
                                                                    question.getTitle(),
                                                                    question.getContent(),
                                                                    question.getFirstCategory(),
                                                                    question.getSecondCategory(),
                                                                    question.getLastCategory(),
                                                                    question.getQuestionStatus()));

        return ApiResponse.of(ApiResponseQuestionEnum.QUESTION_FIND_ALL_SUCCESS, response);
    }

    /**
     * 질문 단건 조회
     *
     * @param questionId 조회할 질문의 ID
     * @since 1.0
     * @throws HandleNotFound 질문 수정 시 데이터가 없을 경우 발생
     * @return 요청한 질문
     * @author 유태이
     */
    public ApiResponse<QuestionResponse.GetQuestion> getQuestion(long questionId) {
        Question question = findById(questionId);
        return ApiResponse.of(ApiResponseQuestionEnum.QUESTION_FIND_ONE_SUCCESS, new QuestionResponse.GetQuestion(question.getId(),
                                                                                                                    question.getTitle(),
                                                                                                                    question.getContent(),
                                                                                                                    question.getFirstCategory(),
                                                                                                                    question.getSecondCategory(),
                                                                                                                    question.getLastCategory(),
                                                                                                                    question.getQuestionStatus()));
    }

    /**
     * 질문 삭제
     *
     * @param questionId 삭제할 질문의 ID
     * @since 1.0
     * @throws HandleNotFound 질문 삭제 시 데이터가 없을 경우 발생
     * @return 삭제 성공 ApiResponse 객체
     * @author 유태이
     */
    public ApiResponse<Void> deleteQuestion(long questionId) {
        Question question = findById(questionId);
        questionRepository.delete(question);
        return ApiResponse.of(ApiResponseQuestionEnum.QUESTION_DELETE_SUCCESS);
    }



    /**
     * 질문 ID로 질문 조회
     *
     * @param questionId 조회할 질문 ID
     * @throws HandleNotFound 질문이 존재하지 않을 경우 발생
     * @return 해당 질문 객체
     */
    public Question findById(long questionId) {
        return questionRepository.findById(questionId).orElseThrow(() -> new HandleNotFound(ApiResponseQuestionEnum.QUESTION_NOT_FOUND));
    }

    /**
     * 주어진 사용자 ID에 해당하는 질문 목록을 반환
     *
     * @param authUser 로그인한 사용자 정보(사용자 ID)
     * @param pageable 페이지 정보(페이지 정보, 페이지 크기)
     * @return 주어진 사용자 ID에 해당하는 질문의 페이징 결과
     */
    public Page<Question> findAllByUserId(AuthUser authUser, Pageable pageable) {
        return questionRepository.findAllByUserId(authUser.getUserId(), pageable);
    }
}

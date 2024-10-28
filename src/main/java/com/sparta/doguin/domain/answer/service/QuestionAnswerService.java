package com.sparta.doguin.domain.answer.service;

import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.answer.dto.AnswerRequest;
import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.entity.Answer;
import com.sparta.doguin.domain.answer.enums.AnswerType;
import com.sparta.doguin.domain.answer.repository.AnswerRepository;
import com.sparta.doguin.domain.common.exception.AnswerException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseAnswerEnum;
import com.sparta.doguin.domain.question.entity.Question;
import com.sparta.doguin.domain.question.repository.QuestionRepository;
import com.sparta.doguin.domain.question.service.QuestionService;
import com.sparta.doguin.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionAnswerService implements AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerType answerType = AnswerType.QUESTION;
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;

    // 답변 생성
    @Override
    @Transactional
    public ApiResponse<AnswerResponse.Response> createQuestionAnswer(AuthUser authUser, long questionId, AnswerRequest.Request request) {
        // 로그인한 사용자의 인증 정보
        User user = User.fromAuthUser(authUser);

        // 질문이 존재하는지 확인
        Question question = questionService.findById(questionId);
        if (question == null) {
            throw new AnswerException(ApiResponseAnswerEnum.QUESTION_NOT_FOUND);
        }

        // 답변 생성
        Answer answer = new Answer(request.content(), user, question, answerType);

        // 답변 저장
        answerRepository.save(answer);

        // 성공 응답 반환
        return ApiResponse.of(ApiResponseAnswerEnum.QUESTION_ANSWER_CREATE_SUCCESS, new AnswerResponse.Response(answer.getId(), answer.getContent()));
    }

    // 답변 수정
    @Override
    @Transactional
    public ApiResponse<AnswerResponse.Response> updateQuestionAnswer(AuthUser authUser, long questionId, long answerId, AnswerRequest.Request request) {
        // 로그인한 사용자의 인증 정보
        User user = User.fromAuthUser(authUser);

        // 해당 질문이 없을 때 예외처리
        Question question = questionService.findById(questionId);
        if (question == null) {
            throw new AnswerException(ApiResponseAnswerEnum.QUESTION_NOT_FOUND);
        }

        // 해당 답변이 없을 때 예외처리
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_ANSWER_NOT_FOUND));

        // 답변이 해당 질문에 속하는지 확인
        if (!answer.getQuestionId().equals(questionId)) {
            throw new AnswerException(ApiResponseAnswerEnum.ANSWER_BELONG_TO_QUESTION);
        }

        // 본인이 생성한 글인지 확인
        if (!answer.getUser().getId().equals(user.getId())) {
            throw new AnswerException(ApiResponseAnswerEnum.UPDATE_ACCESS_DENIED);
        }

        // 대답변 일 경우 수정 불가 **parent_id가 null이 아닐 경우 예외처리
        if (answer.getParent().getId() != null) {
            throw new AnswerException(ApiResponseAnswerEnum.QUESTION_ANSWER_NOT_FOUND);
        }

        // 답변 수정
        answer.update(request);

        // 답변 저장
        answerRepository.save(answer);

        // 성공 응답 반환
        return ApiResponse.of(ApiResponseAnswerEnum.QUESTION_ANSWER_UPDATE_SUCCESS, new AnswerResponse.Response(answer.getId(), answer.getContent()));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Page<AnswerResponse.GetResponse>> viewAllQuestionAnswer(long questionId, int page, int size) {
        Pageable pageable = PageRequest.of(page -1, size);

        // questionId를 사용하여 Question 객체를 조회
        Question question = questionService.findById(questionId);
        if (question == null) {
            throw new AnswerException(ApiResponseAnswerEnum.QUESTION_NOT_FOUND);
        }

        // 해당 질문에 대한 모든 답변 페이징 조회
        Page<Answer> answers = answerRepository.findByQuestion(question, pageable);

        // 대답변을 포함하여 응답 반환
        Page<AnswerResponse.GetResponse> response = answers.map(this::getResponseWithApply);

        return ApiResponse.of(ApiResponseAnswerEnum.QUESTION_ANSWER_FIND_ALL_SUCCESS, response);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<AnswerResponse.GetResponse> viewOneQuestionAnswer(long questionId, long answerId) {

        // 해당 질문이 없을 때 예외처리
        Question question = questionService.findById(questionId);
        if (question == null) {
            throw new AnswerException(ApiResponseAnswerEnum.QUESTION_NOT_FOUND);
        }

        // 해당 답변이 없을 때 예외처리
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_ANSWER_NOT_FOUND));

        // 답변이 해당 질문에 속하는지 확인
        if (!answer.getQuestionId().equals(questionId)) {
            throw new AnswerException(ApiResponseAnswerEnum.ANSWER_BELONG_TO_QUESTION);
        }

        // 대답변을 포함하여 응답 반환
        AnswerResponse.GetResponse response = getResponseWithApply(answer);

        return ApiResponse.of(ApiResponseAnswerEnum.QUESTION_ANSWER_FIND_ONE_SUCCESS, response);
    }




    @Override
    @Transactional
    public ApiResponse<Void> deleteQuestionAnswer(AuthUser authUser, long questionId, long answerId) {
        // 로그인한 사용자의 인증 정보
        User user = User.fromAuthUser(authUser);

        // 해당 질문이 없을 때 예외처리
        Question question = questionService.findById(questionId);
        if (question == null) {
            throw new AnswerException(ApiResponseAnswerEnum.QUESTION_NOT_FOUND);
        }

        // 해당 답변이 없을 때 예외처리
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_ANSWER_NOT_FOUND));

        // 답변이 해당 질문에 속하는지 확인
        if (!answer.getQuestionId().equals(questionId)) {
            throw new AnswerException(ApiResponseAnswerEnum.ANSWER_BELONG_TO_QUESTION);
        }

        // 본인이 생성한 글인지 확인
        if (!answer.getUser().getId().equals(user.getId())) {
            throw new AnswerException(ApiResponseAnswerEnum.UPDATE_ACCESS_DENIED);
        }

        // 대답변 일 경우 수정 불가 **parent_id가 null이 아닐 경우 예외처리
        if (answer.getParent().getId() != null) {
            throw new AnswerException(ApiResponseAnswerEnum.QUESTION_ANSWER_NOT_FOUND);
        }

        // 답변 삭제
        answerRepository.delete(answer);

        // 성공 응답 반환
        return ApiResponse.of(ApiResponseAnswerEnum.QUESTION_ANSWER_DELETE_SUCCESS);
    }



    /**
     * 주어진 답변에 대한 응답 객체를 생성하고, 하위 답변(대답변) 목록을 포함
     * .
     * parent_id가 주어진 답변의 ID와 일치하는 모든 대답변을 조회하고
     * GetResponse로 변환하여 빈 하위 답변 리스트에 포함
     *
     * @param answer 답변 엔티티
     * @return 부모 답변 정보와 하위 답변 목록을 포함한 응답 객체
     * @since 1.0
     * @author 유태이
     */
    private AnswerResponse.GetResponse getResponseWithApply(Answer answer) {
        List<AnswerResponse.GetResponse> applyResponse = answerRepository.findByParentId(answer.getId())
                .stream()
                .map(apply -> new AnswerResponse.GetResponse(apply.getId(), apply.getContent(), new ArrayList<>()))
                .toList();

        return new AnswerResponse.GetResponse(answer.getId(), answer.getContent(), applyResponse);
    }




















}

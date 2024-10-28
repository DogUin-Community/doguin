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

    // 답변 생성
    @Override
    @Transactional
    public ApiResponse<AnswerResponse.Response> createQuestionAnswer(AuthUser authUser, long questionId, AnswerRequest.Request request) {
        // 로그인한 사용자의 인증 정보
        User user = User.fromAuthUser(authUser);

        // 질문 찾기
        Answer findAnswer = answerRepository.findFirstByQuestionId(questionId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_NOT_FOUND));

        // Question 가져오기
        Question question = findAnswer.getQuestion();

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

        // 질문 찾기
        Answer findAnswer = answerRepository.findFirstByQuestionId(questionId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_NOT_FOUND));

        // Question 가져오기
        Question question = findAnswer.getQuestion();

        // 답변 찾기
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_ANSWER_NOT_FOUND));

        // parentId가 있을 경우 수정 불가
        if (answer.getParent() != null) {
            throw new AnswerException(ApiResponseAnswerEnum.QUESTION_ANSWER_NOT_FOUND);
        }

        // 본인이 생성한 글인지 확인
        if (!answer.getUser().getId().equals(user.getId())) {
            throw new AnswerException(ApiResponseAnswerEnum.UPDATE_ACCESS_DENIED);
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

        // 질문 찾기
        Answer findAnswer = answerRepository.findFirstByQuestionId(questionId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_NOT_FOUND));

        // Question 가져오기
        Question question = findAnswer.getQuestion();

        Page<Answer> answers = answerRepository.findByQuestionId(questionId, pageable);

        Page<AnswerResponse.GetResponse> response = answers
                .map(answer -> {
                    List<AnswerResponse.GetResponse> applyResponse = answerRepository.findByParentId(answer.getId()).stream()
                            .map(applyAnswer -> new AnswerResponse.GetResponse(applyAnswer.getId(), applyAnswer.getContent(), new ArrayList<>())).toList();

                    // 현재 답변과 대답변 리스트를 포함한 응답
                    return new AnswerResponse.GetResponse(answer.getId(), answer.getContent(), applyResponse);
                });

        return ApiResponse.of(ApiResponseAnswerEnum.QUESTION_ANSWER_FIND_ALL_SUCCESS, response);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<AnswerResponse.GetResponse> viewOneQuestionAnswer(long questionId, long answerId) {
        // 질문 찾기
        Answer findAnswer = answerRepository.findFirstByQuestionId(questionId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_NOT_FOUND));

        // Question 가져오기
        Question question = findAnswer.getQuestion();

        // 답변 찾기
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_ANSWER_NOT_FOUND));

        List<AnswerResponse.GetResponse> applyresponse = answerRepository.findByParentId(answer.getId()).stream()
                .map(applyAnswer -> new AnswerResponse.GetResponse(applyAnswer.getId(), applyAnswer.getContent(), new ArrayList<>())).toList();

        AnswerResponse.GetResponse response = new AnswerResponse.GetResponse(answer.getId(), answer.getContent(), applyresponse);

        return ApiResponse.of(ApiResponseAnswerEnum.QUESTION_ANSWER_FIND_ONE_SUCCESS, response);
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteQuestionAnswer(AuthUser authUser, long questionId, long answerId) {
        // 로그인한 사용자의 인증 정보
        User user = User.fromAuthUser(authUser);

        // 질문 찾기
        Answer findAnswer = answerRepository.findFirstByQuestionId(questionId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_NOT_FOUND));

        // Question 가져오기
        Question question = findAnswer.getQuestion();

        // 답변 찾기
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_ANSWER_NOT_FOUND));

        // parentId가 있을 경우 수정 불가
        if (answer.getParent() != null) {
            throw new AnswerException(ApiResponseAnswerEnum.QUESTION_ANSWER_NOT_FOUND);
        }

        // 본인이 생성한 글인지 확인
        if (!answer.getUser().getId().equals(user.getId())) {
            throw new AnswerException(ApiResponseAnswerEnum.UPDATE_ACCESS_DENIED);
        }

        // 답변 삭제
        answerRepository.delete(answer);

        // 성공 응답 반환
        return ApiResponse.of(ApiResponseAnswerEnum.QUESTION_ANSWER_DELETE_SUCCESS);
    }




















}

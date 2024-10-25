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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplyAnswerService implements AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerType answerType = AnswerType.QUESTION;

    @Override
    @Transactional
    public ApiResponse<AnswerResponse.Response> createApplyAnswer(AuthUser authUser, long questionId, long answerId, AnswerRequest.Request request) {
        // 로그인한 사용자의 인증 정보
        User user = User.fromAuthUser(authUser);

        // 질문 찾기
        Answer findAnswer = answerRepository.findFirstByQuestionId(questionId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_NOT_FOUND));

        // Question 가져오기
        Question question = findAnswer.getQuestion();

        // 상위 댓글 찾기
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_ANSWER_NOT_FOUND));

        // 본인이 생성한 글인지 확인
        if (!answer.getUser().getId().equals(user.getId())) {
            throw new AnswerException(ApiResponseAnswerEnum.UPDATE_ACCESS_DENIED);
        }

        // 답변 생성
        Answer newAnswer = new Answer(request.content(), user, question, answerType);

        // 답변 저장
        answerRepository.save(newAnswer);

        // 성공 응답 반환
        return ApiResponse.of(ApiResponseAnswerEnum.APPLY_ANSWER_CREATE_SUCCESS, new AnswerResponse.Response(newAnswer.getId(), newAnswer.getContent()));
    }

    @Override
    @Transactional
    public ApiResponse<AnswerResponse.Response> updateApplyAnswer(AuthUser authUser, long questionId, long parentId, long answerId, AnswerRequest.Request request) {
        // 로그인 한 사용자의 인증 정보
        User user = User.fromAuthUser(authUser);

        // 질문 찾기
        Answer findAnswer = answerRepository.findFirstByQuestionId(questionId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_NOT_FOUND));

        // Question 가져오기
        Question question = findAnswer.getQuestion();

        // 상위 댓글 찾기
        Answer parentAnswer = answerRepository.findById(parentId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_ANSWER_NOT_FOUND));

        // 대답변 찾기
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_ANSWER_NOT_FOUND));

        // 해당 대답변이 상위 댓글에 속하는지 검증
        if (answer.getParent() == null || !answer.getParent().getId().equals(parentAnswer.getId())) {
            throw new AnswerException(ApiResponseAnswerEnum.APPLY_ANSWER_NOT_FOUND);
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
        return ApiResponse.of(ApiResponseAnswerEnum.APPLY_ANSWER_UPDATE_SUCCESS, new AnswerResponse.Response(answer.getId(), answer.getContent()));
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteApplyAnswer(AuthUser authUser, long questionId, long parentId, long answerId) {
        // 로그인 한 사용자의 인증 정보
        User user = User.fromAuthUser(authUser);


        // 질문 찾기
        Answer findAnswer = answerRepository.findFirstByQuestionId(questionId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_NOT_FOUND));

        Question question = findAnswer.getQuestion();

        // 상위 댓글 찾기
        Answer parentAnswer = answerRepository.findById(parentId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_ANSWER_NOT_FOUND));

        // 대답변 찾기
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.QUESTION_ANSWER_NOT_FOUND));

        // 본인이 생성한 글인지 확인
        if (!answer.getUser().getId().equals(user.getId())) {
            throw new AnswerException(ApiResponseAnswerEnum.UPDATE_ACCESS_DENIED);
        }

        // 해당 대답변이 상위 댓글에 속하는지 검증
        if (answer.getParent() == null || !answer.getParent().getId().equals(parentAnswer.getId())) {
            throw new AnswerException(ApiResponseAnswerEnum.APPLY_ANSWER_NOT_FOUND);
        }

        // 대댓글 삭제
        answerRepository.delete(answer);

        // 성공 응답 반환
        return ApiResponse.of(ApiResponseAnswerEnum.APPLY_ANSWER_DELETE_SUCCESS);
    }

}

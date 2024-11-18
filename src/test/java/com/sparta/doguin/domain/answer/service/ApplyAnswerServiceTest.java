package com.sparta.doguin.domain.answer.service;

import com.sparta.doguin.domain.answer.dto.AnswerRequest;
import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.entity.Answer;
import com.sparta.doguin.domain.answer.enums.AnswerType;
import com.sparta.doguin.domain.answer.repository.AnswerRepository;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.question.entity.Question;
import com.sparta.doguin.domain.question.service.QuestionService;
import com.sparta.doguin.domain.setup.DataUtil;
import com.sparta.doguin.security.AuthUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class ApplyAnswerServiceTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private ApplyAnswerService applyAnswerService;

    @Test
    void 대댓글_생성_성공() {
        // given
        AuthUser authUser = DataUtil.authUser1();
        long questionId = DataUtil.one();
        long answerId = DataUtil.one();
        Question question = DataUtil.question1();
        Answer parentAnswer = DataUtil.answer1();
        AnswerRequest.Request request = new AnswerRequest.Request("대댓글 내용");

        given(questionService.findById(questionId)).willReturn(question);
        given(answerRepository.findById(answerId)).willReturn(Optional.of(parentAnswer));

        // when
        ApiResponse<AnswerResponse.Response> response = applyAnswerService.createApplyAnswer(authUser, questionId, answerId, request);

        // then
        assertEquals("질문 대답변 등록에 성공하였습니다.", response.getMessage());
    }

    @Test
    void 대댓글_수정_성공() {
        // given
        AuthUser authUser = DataUtil.authUser1();
        long questionId = DataUtil.one();
        long parentId = DataUtil.one();
        long answerId = DataUtil.two();
        Question question = DataUtil.question1();
        Answer parentAnswer = DataUtil.answer1();
        Answer answer = new Answer("기존 답변 내용", DataUtil.user1(), question, parentAnswer, AnswerType.QUESTION);
        AnswerRequest.Request request = new AnswerRequest.Request("수정된 답변 내용");

        setField(parentAnswer, "id", parentId);
        setField(answer, "id", answerId);

        given(questionService.findById(questionId)).willReturn(question);
        given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));
        given(answerRepository.findById(parentId)).willReturn(Optional.of(parentAnswer));

        // when
        ApiResponse<AnswerResponse.Response> response = applyAnswerService.updateApplyAnswer(authUser, questionId, parentId, answerId, request);

        // then
        assertEquals("질문 대답변 수정에 성공하셨습니다.", response.getMessage());
    }

    @Test
    void 대댓글_삭제_성공() {
        // given
        AuthUser authUser = DataUtil.authUser1();
        long questionId = DataUtil.one();
        long parentId = DataUtil.one();
        long answerId = DataUtil.two();
        Question question = DataUtil.question1();
        Answer parentAnswer = DataUtil.answer1();
        Answer answer = new Answer("답변 내용", DataUtil.user1(), question, parentAnswer, AnswerType.QUESTION);

        setField(parentAnswer, "id", parentId);
        setField(answer, "id", answerId);

        given(questionService.findById(questionId)).willReturn(question);
        given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));
        given(answerRepository.findById(parentId)).willReturn(Optional.of(parentAnswer));

        // when
        ApiResponse<Void> response = applyAnswerService.deleteApplyAnswer(authUser, questionId, parentId, answerId);

        // then
        assertEquals("질문 대답변 삭제에 성공하였습니다.", response.getMessage());
    }


}

package com.sparta.doguin.domain.answer.service;

import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.answer.dto.AnswerRequest;
import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.entity.Answer;
import com.sparta.doguin.domain.answer.enums.AnswerType;
import com.sparta.doguin.domain.answer.repository.AnswerRepository;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.question.entity.Question;
import com.sparta.doguin.domain.setup.DataUtil;
import com.sparta.doguin.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ApplyAnswerServiceTest {

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private ApplyAnswerService applyAnswerService;

    @Test
    void 대댓글_생성_성공() {
        // given
        long questionId = DataUtil.one();
        long answerId = DataUtil.two();
        AuthUser authUser = DataUtil.authUser1();
        AnswerRequest.Request request = new AnswerRequest.Request("대댓글 내용");

        Question question = DataUtil.question1();
        User user = DataUtil.user1();
        Answer parentAnswer = new Answer("상위 댓글", user, question, AnswerType.QUESTION);

        given(answerRepository.findFirstByQuestionId(questionId)).willReturn(Optional.of(parentAnswer));
        given(answerRepository.findById(answerId)).willReturn(Optional.of(parentAnswer));

        Answer newAnswer = new Answer(request.content(), user, question, AnswerType.QUESTION);
        given(answerRepository.save(any(Answer.class))).willReturn(newAnswer);

        // when
        ApiResponse<AnswerResponse.Response> response = applyAnswerService.createApplyAnswer(authUser, questionId, answerId, request);

        // then
        assertEquals("질문 대답변 등록에 성공하였습니다.", response.getMessage());
    }

    @Test
    void 대댓글_수정_성공() {
        // given
        long questionId = DataUtil.one();
        long parentId = DataUtil.two();
        long answerId = DataUtil.two();
        AuthUser authUser = DataUtil.authUser1();
        AnswerRequest.Request request = new AnswerRequest.Request("수정된 대댓글 내용");

        Question question = DataUtil.question1();
        User user = DataUtil.user1();
        Answer parentAnswer = new Answer("상위 댓글", user, question, AnswerType.QUESTION);
        Answer answer = new Answer("기존 대댓글 내용", user, question, parentAnswer, AnswerType.QUESTION);

        ReflectionTestUtils.setField(parentAnswer, "id", parentId);
        ReflectionTestUtils.setField(answer, "id", answerId);

        given(answerRepository.findFirstByQuestionId(questionId)).willReturn(Optional.of(parentAnswer));
        given(answerRepository.findById(parentId)).willReturn(Optional.of(parentAnswer));
        given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));

        // when
        ApiResponse<AnswerResponse.Response> response = applyAnswerService.updateApplyAnswer(authUser, questionId, parentId, answerId, request);

        // then
        assertEquals("질문 대답변 수정에 성공하셨습니다.", response.getMessage());
    }

    @Test
    void 대댓글_삭제_성공() {
        // given
        long questionId = DataUtil.one();
        long parentId = DataUtil.two();
        long answerId = DataUtil.two();
        AuthUser authUser = DataUtil.authUser1();

        Question question = DataUtil.question1();
        User user = DataUtil.user1();
        Answer parentAnswer = new Answer("상위 댓글", user, question, AnswerType.QUESTION);
        Answer answer = new Answer("기존 대댓글 내용", user, question, parentAnswer, AnswerType.QUESTION);

        ReflectionTestUtils.setField(parentAnswer, "id", parentId);
        ReflectionTestUtils.setField(answer, "id", answerId);

        given(answerRepository.findFirstByQuestionId(questionId)).willReturn(Optional.of(parentAnswer));
        given(answerRepository.findById(parentId)).willReturn(Optional.of(parentAnswer));
        given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));

        // when
        ApiResponse<Void> response = applyAnswerService.deleteApplyAnswer(authUser, questionId, parentId, answerId);

        // then
        assertEquals("질문 대답변 삭제에 성공하였습니다.", response.getMessage());
    }


}

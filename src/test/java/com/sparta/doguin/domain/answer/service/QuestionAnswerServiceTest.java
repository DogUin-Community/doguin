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
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.security.AuthUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
public class QuestionAnswerServiceTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private QuestionAnswerService answerService;

    @Test
    void 답변_생성_성공() {
        // given
        AuthUser authUser = DataUtil.authUser1();
        long questionId = DataUtil.one();
        Question question = DataUtil.question1();
        AnswerRequest.Request request = new AnswerRequest.Request("새로운 답변 내용");

        given(questionService.findById(questionId)).willReturn(question);

        // when
        ApiResponse<AnswerResponse.Response> response = answerService.createQuestionAnswer(authUser, questionId, request);

        //then
        assertEquals("질문 답변 등록에 성공하였습니다.", response.getMessage());
    }

    @Test
    void 답변_수정_성공() {
        // given
        AuthUser authUser = DataUtil.authUser1();
        long questionId = DataUtil.one();
        long answerId = DataUtil.two();
        Question question = DataUtil.question1();
        User user = DataUtil.user1();

        // 기존 답변 설정
        Answer answer = new Answer("기존 답변 내용", user, question, AnswerType.QUESTION);

        // 리플렉션을 사용해 ID 설정 및 관계 설정
        setField(question, "id", questionId);
        setField(answer, "id", answerId);
        setField(answer, "question", question);
        setField(answer, "user", user);
        setField(answer, "parent", null);

        AnswerRequest.Request request = new AnswerRequest.Request("수정된 답변 내용");

        // Mock 객체 설정
        given(questionService.findById(questionId)).willReturn(question);
        given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));

        // when
        ApiResponse<AnswerResponse.Response> response = answerService.updateQuestionAnswer(authUser, questionId, answerId, request);

        // then
        assertEquals("질문 답변 수정에 성공하셨습니다.", response.getMessage());
    }

    @Test
    void 전체_답변_조회_성공() {
        // given
        long questionId = DataUtil.one();
        int page = 1;
        int size = 10;
        Question question = DataUtil.question1();
        Answer answer1 = DataUtil.answer1();
        Answer answer2 = new Answer("두 번째 답변 내용", DataUtil.user1(), question, AnswerType.QUESTION);

        setField(question, "id", questionId);
        setField(answer1, "id", DataUtil.one());
        setField(answer2, "id", DataUtil.two());

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Answer> answers = new PageImpl<>(List.of(answer1, answer2), pageable, 2);

        given(questionService.findById(questionId)).willReturn(question);
        given(answerRepository.findByQuestion(question, pageable)).willReturn(answers);

        // when
        ApiResponse<Page<AnswerResponse.GetResponse>> response = answerService.viewAllQuestionAnswer(questionId, page, size);

        // then
        assertEquals("질문 답변 조회(전체)에 성공하였습니다.", response.getMessage());
    }

    @Test
    void 단일_답변_조회_성공() {
        // given
        long questionId = DataUtil.one();
        long answerId = DataUtil.two();
        Question question = DataUtil.question1();
        Answer answer = DataUtil.answer1();

        setField(answer, "id", answerId);

        given(questionService.findById(questionId)).willReturn(question);
        given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));

        // when
        ApiResponse<AnswerResponse.GetResponse> response = answerService.viewOneQuestionAnswer(questionId, answerId);

        // then
        assertEquals("질문 답변 조회(단건)에 성공하였습니다.", response.getMessage());
    }

    @Test
    void 답변_삭제_성공() {
        // given
        AuthUser authUser = DataUtil.authUser1();
        long questionId = DataUtil.one();
        long answerId = DataUtil.two();
        Question question = DataUtil.question1();
        User user = DataUtil.user1();
        Answer answer = new Answer("삭제할 답변 내용", user, question, AnswerType.QUESTION);

        setField(question, "id", questionId);
        setField(answer, "id", answerId);
        setField(answer, "question", question);
        setField(answer, "user", user);
        setField(answer, "parent", null);

        given(questionService.findById(questionId)).willReturn(question);
        given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));

        // when
        ApiResponse<Void> response = answerService.deleteQuestionAnswer(authUser, questionId, answerId);

        // then
        assertEquals("질문 답변 삭제에 성공하였습니다.", response.getMessage());
    }
}

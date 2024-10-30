//package com.sparta.doguin.domain.answer.service;
//
//import com.sparta.doguin.security.AuthUser;
//import com.sparta.doguin.domain.answer.dto.AnswerRequest;
//import com.sparta.doguin.domain.answer.dto.AnswerResponse;
//import com.sparta.doguin.domain.answer.entity.Answer;
//import com.sparta.doguin.domain.answer.enums.AnswerType;
//import com.sparta.doguin.domain.answer.repository.AnswerRepository;
//import com.sparta.doguin.domain.common.response.ApiResponse;
//import com.sparta.doguin.domain.question.entity.Question;
//import com.sparta.doguin.domain.setup.DataUtil;
//import com.sparta.doguin.domain.user.entity.User;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//public class QuestionAnswerServiceTest {
//
//    @Mock
//    private AnswerRepository answerRepository;
//
//    @InjectMocks
//    private QuestionAnswerService questionAnswerService;
//
//    @Test
//    void 답변_생성_성공() {
//        // given
//        long questionId = DataUtil.one();
//        AuthUser authUser = DataUtil.authUser1();
//        AnswerRequest.Request request = new AnswerRequest.Request("content");
//
//        Question question = DataUtil.question1();
//        User user = DataUtil.user1();
//
//        given(answerRepository.findFirstByQuestionId(questionId)).willReturn(Optional.of(new Answer()));
//        Answer answer = new Answer(request.content(), user, question, AnswerType.QUESTION);
//
//        // when
//        given(answerRepository.save(any(Answer.class))).willReturn(answer);
//        ApiResponse<AnswerResponse.Response> response = questionAnswerService.createQuestionAnswer(authUser, questionId, request);
//
//        //then
//        assertEquals("질문 답변 등록에 성공하였습니다.", response.getMessage());
//    }
//
//    @Test
//    void 답변_수정_성공() {
//        // given
//        AuthUser authUser = DataUtil.authUser1();
//        long questionId = DataUtil.one();
//        long answerId = DataUtil.two();
//        AnswerRequest.Request request = new AnswerRequest.Request("수정된 답변 내용");
//
//        Question question = DataUtil.question1();
//        User user = DataUtil.user1();
//        Answer answer = new Answer("기존 답변 내용", user, question, AnswerType.QUESTION);
//
//        // given
//        given(answerRepository.findFirstByQuestionId(questionId)).willReturn(Optional.of(new Answer()));
//        given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));
//
//        // when
//        ApiResponse<AnswerResponse.Response> response = questionAnswerService.updateQuestionAnswer(authUser, questionId, answerId, request);
//
//        // then
//        assertEquals("질문 답변 수정에 성공하셨습니다.", response.getMessage());
//    }
//
//    @Test
//    void 전체_답변_조회_성공() {
//        // Given: 페이지 요청 및 답변 목록 데이터 준비
//        long questionId = DataUtil.one();
//        Pageable pageable = PageRequest.of(0, 10);
//
//        Answer answer = DataUtil.answer1();
//        Page<Answer> answers = new PageImpl<>(List.of(answer));
//
//        given(answerRepository.findFirstByQuestionId(questionId)).willReturn(Optional.of(answer));
//        given(answerRepository.findByQuestionId(questionId, pageable)).willReturn(answers);
//
//        // When: 모든 답변을 조회하도록 서비스 호출
//        ApiResponse<Page<AnswerResponse.GetResponse>> response = questionAnswerService.viewAllQuestionAnswer(questionId, 1, 10);
//
//        // Then: 답변 조회 성공 응답 확인
//        assertEquals("질문 답변 조회(전체)에 성공하였습니다.", response.getMessage());
//    }
//
//    @Test
//    void 단일_답변_조회_성공() {
//        // Given: 질문 ID 및 답변 ID 데이터 준비
//        long questionId = DataUtil.one();
//        long answerId = DataUtil.two();
//        Answer answer = DataUtil.answer1();
//
//        given(answerRepository.findFirstByQuestionId(questionId)).willReturn(Optional.of(answer));
//        given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));
//
//        // When: 단일 답변을 조회하도록 서비스 호출
//        ApiResponse<AnswerResponse.GetResponse> response = questionAnswerService.viewOneQuestionAnswer(questionId, answerId);
//
//        // Then: 단일 답변 조회 성공 응답 확인
//        assertEquals("질문 답변 조회(단건)에 성공하였습니다.", response.getMessage());
//    }
//
//    @Test
//    void 답변_삭제_성공() {
//        // Given: 인증된 사용자, 질문 ID 및 답변 ID 데이터 준비
//        AuthUser authUser = DataUtil.authUser1();
//        long questionId = DataUtil.one();
//        long answerId = DataUtil.two();
//
//        Question question = DataUtil.question1();
//        User user = DataUtil.user1();
//        Answer answer = new Answer("답변 내용", user, question, AnswerType.QUESTION);
//
//        given(answerRepository.findFirstByQuestionId(questionId)).willReturn(Optional.of(answer));
//        given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));
//
//        // When: 답변을 삭제하도록 서비스 호출
//        ApiResponse<Void> response = questionAnswerService.deleteQuestionAnswer(authUser, questionId, answerId);
//
//        // Then: 답변 삭제 성공 응답 확인
//        assertEquals("질문 답변 삭제에 성공하였습니다.", response.getMessage());
//    }
//}

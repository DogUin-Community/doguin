package com.sparta.doguin.domain.question.service;

import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.answer.entity.Answer;
import com.sparta.doguin.domain.answer.repository.AnswerRepository;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.question.dto.QuestionRequest;
import com.sparta.doguin.domain.question.dto.QuestionResponse;
import com.sparta.doguin.domain.question.entity.Question;
import com.sparta.doguin.domain.question.enums.FirstCategory;
import com.sparta.doguin.domain.question.enums.LastCategory;
import com.sparta.doguin.domain.question.enums.SecondCategory;
import com.sparta.doguin.domain.question.repository.QuestionRepository;
import com.sparta.doguin.domain.setup.DataUtil;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void 질문_생성_성공() {
        // given
        AuthUser authUser = DataUtil.authUser1();
        QuestionRequest.CreatedQuestion request = DataUtil.questionRequestCreate1();

        // when
        given(questionRepository.save(any(Question.class))).willReturn(DataUtil.question1());
        ApiResponse<QuestionResponse.CreatedQuestion> response = questionService.createdQuestion(authUser, request);

        // then
        assertEquals("질문 등록에 성공하였습니다.", response.getMessage());

    }

    @Test
    void 질문_수정_성공() {
        // given
        long questionId = DataUtil.one();

        AuthUser authUser = DataUtil.authUser1();
        QuestionRequest.UpdateQuestion request = new QuestionRequest.UpdateQuestion("title", "content", FirstCategory.JAVA, SecondCategory.STRING, LastCategory.REDIS);

        Question question = new Question(
                questionId,
                DataUtil.questionRequestCreate1().title(),
                DataUtil.questionRequestCreate1().content(),
                DataUtil.questionRequestCreate1().firstCategory(),
                DataUtil.questionRequestCreate1().secondCategory(),
                DataUtil.questionRequestCreate1().lastCategory(),
                DataUtil.user1()
        );

        // when
        given(questionRepository.findById(questionId)).willReturn(Optional.of(question));
        given(questionRepository.save(any(Question.class))).willReturn(question);

        ApiResponse<QuestionResponse.CreatedQuestion> response = questionService.updatedQuestion(authUser, questionId, request);

        // then
        assertEquals("질문 수정에 성공하셨습니다.", response.getMessage());
    }

    @Test
    void 질문_전체_조회() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Question question = DataUtil.question1();
        Page<Question> questions = new PageImpl<>(List.of(question));

        // when
        given(questionRepository.findAll(pageable)).willReturn(questions);
        ApiResponse<Page<QuestionResponse.GetQuestions>> response = questionService.getQuestions(1, 10);

        // then
        assertEquals("질문 조회(전체)에 성공하였습니다.", response.getMessage());
        assertNotNull(response.getData());
    }

    @Test
    void 질문_단건_조회() {
        // given
        long questionId = DataUtil.one();
        Question question = DataUtil.question1();
        Page<Answer> answers = new PageImpl<>(List.of(new Answer(/* 필요한 데이터 초기화 */)));
        Pageable pageable = PageRequest.of(0, 10);

        // when
        given(questionRepository.findById(questionId)).willReturn(Optional.of(question));
        given(answerRepository.findByQuestionId(questionId, pageable)).willReturn(answers);
        ApiResponse<QuestionResponse.GetQuestion> response = questionService.getQuestion(questionId);

        // then
        assertEquals("질문 조회(단건)에 성공하였습니다.", response.getMessage());
        assertNotNull(response.getData());
    }

    @Test
    void 질문_삭제_성공() {
        // given
        AuthUser authUser = DataUtil.authUser1();
        long questionId = DataUtil.one();

        Question question = new Question(
                questionId,
                DataUtil.questionRequestCreate1().title(),
                DataUtil.questionRequestCreate1().content(),
                DataUtil.questionRequestCreate1().firstCategory(),
                DataUtil.questionRequestCreate1().secondCategory(),
                DataUtil.questionRequestCreate1().lastCategory(),
                DataUtil.user1()
        );

        // when
        given(questionRepository.findById(questionId)).willReturn(Optional.of(question));
        ApiResponse<Void> response = questionService.deleteQuestion(authUser, questionId);

        // then
        assertEquals("질문 삭제에 성공하였습니다.", response.getMessage());
    }



}

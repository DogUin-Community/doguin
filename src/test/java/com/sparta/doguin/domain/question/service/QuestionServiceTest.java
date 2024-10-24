package com.sparta.doguin.domain.question.service;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.question.dto.QuestionRequest;
import com.sparta.doguin.domain.question.dto.QuestionResponse;
import com.sparta.doguin.domain.question.entity.Question;
import com.sparta.doguin.domain.question.enums.FirstCategory;
import com.sparta.doguin.domain.question.enums.LastCategory;
import com.sparta.doguin.domain.question.enums.SecondCategory;
import com.sparta.doguin.domain.question.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void 질문_생성_성공_시() {
        QuestionRequest.CreatedQuestion request = new QuestionRequest.CreatedQuestion("질문 제목", "질문 본문", FirstCategory.JAVA, SecondCategory.STRING, LastCategory.REDIS);
        Question newQuestion = new Question(1L, "질문 제목", "질문 본문", FirstCategory.JAVA, SecondCategory.STRING, LastCategory.REDIS);

        given(questionRepository.save(any(Question.class))).willReturn(newQuestion);

        ApiResponse<QuestionResponse.CreatedQuestion> response = questionService.createdQuestion(request);

        assertEquals(newQuestion.getTitle(), response.getData().title());
        assertEquals(newQuestion.getContent(), response.getData().content());
        assertEquals(newQuestion.getFirstCategory(), response.getData().firstCategory());
        assertEquals(newQuestion.getSecondCategory(), response.getData().secondCategory());
        assertEquals(newQuestion.getLastCategory(), response.getData().lastCategory());
    }

    @Test
    void 질문_수정_성공() {
        long questionId = 1L;
        QuestionRequest.UpdateQuestion request = new QuestionRequest.UpdateQuestion("수정 된 제목", "수정 된 본문", FirstCategory.JAVA, SecondCategory.STRING, LastCategory.REDIS);
        Question prevQuestion = new Question(questionId, "원래 제목", "원래 본문", FirstCategory.JAVASCRIPT, SecondCategory.STRING, LastCategory.REDIS);

        prevQuestion.update(request);

        given(questionRepository.findById(questionId)).willReturn(Optional.of(prevQuestion));
        given(questionRepository.save(prevQuestion)).willReturn(prevQuestion);

        ApiResponse<QuestionResponse.CreatedQuestion> response = questionService.updatedQuestion(questionId, request);

        assertEquals(request.title(), response.getData().title());
        assertEquals(request.content(), response.getData().content());
        assertEquals(request.firstCategory(), response.getData().firstCategory());
        assertEquals(request.secondCategory(), response.getData().secondCategory());
        assertEquals(request.lastCategory(), response.getData().lastCategory());
    }

    @Test
    void 질문_다건_조회_성공() {
        int page = 1;
        int size = 10;

        // 테스트용 질문 목록 생성
        List<Question> questionList = List.of(
                new Question(1L, "질문 제목 1", "질문 본문 1", FirstCategory.JAVASCRIPT, SecondCategory.STRING, LastCategory.REDIS),
                new Question(2L, "질문 제목 2", "질문 본문 2", FirstCategory.JAVA, SecondCategory.STRING, LastCategory.REDIS)
        );

        // Page 객체 생성
        Page<Question> questionPage = new PageImpl<>(questionList);

        given(questionRepository.findAll(any(Pageable.class))).willReturn(questionPage);

        // 목록 조회 요청
        ApiResponse<Page<QuestionResponse.GetQuestions>> response = questionService.getQuestions(page, size);

        assertEquals(2, response.getData().getTotalElements());
        assertEquals(questionList.size(), response.getData().getContent().size());
    }

    @Test
    void 질문_단건_조회_성공() {
        long questionId = 1L;
        Question question = new Question(questionId, "질문 제목", "질문 본문", FirstCategory.JAVASCRIPT, SecondCategory.STRING, LastCategory.REDIS);

        given(questionRepository.findById(questionId)).willReturn(Optional.of(question));

        ApiResponse<QuestionResponse.GetQuestion> response = questionService.getQuestion(questionId);

        assertEquals(question.getTitle(), response.getData().title());
        assertEquals(question.getContent(), response.getData().content());
        assertEquals(question.getFirstCategory(), response.getData().firstCategory());
        assertEquals(question.getSecondCategory(), response.getData().secondCategory());
        assertEquals(question.getLastCategory(), response.getData().lastCategory());
    }

    @Test
    void 질문_삭제_성공() {
        long questionId = 1L;
        Question question = new Question(questionId, "질문 제목", "질문 본문", FirstCategory.JAVASCRIPT, SecondCategory.STRING, LastCategory.REDIS);

        given(questionRepository.findById(questionId)).willReturn(Optional.of(question));

        ApiResponse<Void> response = questionService.deleteQuestion(questionId);

        assertEquals("질문 삭제에 성공하였습니다.", response.getMessage());
    }


}

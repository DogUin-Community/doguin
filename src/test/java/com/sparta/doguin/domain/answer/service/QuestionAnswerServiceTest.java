package com.sparta.doguin.domain.answer.service;

import com.sparta.doguin.domain.answer.AnswerType;
import com.sparta.doguin.domain.answer.dto.AnswerRequest;
import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.entity.Answer;
import com.sparta.doguin.domain.answer.repository.AnswerRepository;
import com.sparta.doguin.domain.common.response.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class QuestionAnswerServiceTest {

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private QuestionAnswerService questionAnswerService;

    @Test
    void 질문_답변_등록_생성_성공_시() {
        AnswerRequest.Request request = new AnswerRequest.Request("답변 본문");
        Answer answer = new Answer("답변 본문", AnswerType.APPLY_ANSWER);

        given(answerRepository.save(any(Answer.class))).willReturn(answer);

        ApiResponse<AnswerResponse.Response> response = questionAnswerService.create(request);

        assertEquals(answer.getContent(), response.getData().content());
    }

    @Test
    void 질문_답변_수정_성공_시() {
        AnswerRequest.Request request = new AnswerRequest.Request("수정 된 답변 본문");
        Answer answer = new Answer("답변 본문", AnswerType.APPLY_ANSWER);

        given(answerRepository.findById(anyLong())).willReturn(Optional.of(answer));

        ApiResponse<AnswerResponse.Response> response = questionAnswerService.update(1L, request);

        assertEquals("수정 된 답변 본문", answer.getContent());
        assertEquals("수정 된 답변 본문", response.getData().content());
    }

    @Test
    void 질문_답변_단건_조회_성공_시() {
        long answerId = 1L;
        Answer answer = new Answer("답변 본문", AnswerType.APPLY_ANSWER);

        given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));

        ApiResponse<AnswerResponse.Response> response = questionAnswerService.viewOne(answerId);

        assertEquals(answer.getContent(), response.getData().content());
    }

    @Test
    void 질문_답변_삭제_성공_시() {
        Answer answer = new Answer("답변 본문", AnswerType.APPLY_ANSWER);

        given(answerRepository.findById(anyLong())).willReturn(Optional.of(answer));

        ApiResponse<Void> response = questionAnswerService.delete(1L);

        assertEquals("질문 답변 삭제에 성공하였습니다.", response.getMessage());
    }
}

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
public class BulletinAnswerServiceTest {
    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private BulletinAnswerService bulletinAnswerService;

    @Test
    void 자유게시판_댓글_등록_생성_성공_시() {
        AnswerRequest.Request request = new AnswerRequest.Request("댓글 본문");
        Answer answer = new Answer("댓글 본문", AnswerType.APPLY_ANSWER);

        given(answerRepository.save(any(Answer.class))).willReturn(answer);

        ApiResponse<AnswerResponse.Response> response = bulletinAnswerService.create(request);

        assertEquals(answer.getContent(), response.getData().content());
    }

    @Test
    void 자유게시판_댓글_수정_성공_시() {
        AnswerRequest.Request request = new AnswerRequest.Request("수정 된 댓글 본문");
        Answer answer = new Answer("댓글 본문", AnswerType.APPLY_ANSWER);

        given(answerRepository.findById(anyLong())).willReturn(Optional.of(answer));

        ApiResponse<AnswerResponse.Response> response = bulletinAnswerService.update(1L, request);

        assertEquals("수정 된 댓글 본문", answer.getContent());
        assertEquals("수정 된 댓글 본문", response.getData().content());
    }

    @Test
    void 자유게시판_댓글_단건_조회_성공_시() {
        long answerId = 1L;
        Answer answer = new Answer("댓글 본문", AnswerType.APPLY_ANSWER);

        given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));

        ApiResponse<AnswerResponse.Response> response = bulletinAnswerService.viewOne(answerId);

        assertEquals(answer.getContent(), response.getData().content());
    }

    @Test
    void 자유게시판_댓글_삭제_성공_시() {
        Answer answer = new Answer("댓글 본문", AnswerType.APPLY_ANSWER);

        given(answerRepository.findById(anyLong())).willReturn(Optional.of(answer));

        ApiResponse<Void> response = bulletinAnswerService.delete(1L);

        assertEquals("댓글 삭제에 성공하였습니다.", response.getMessage());
    }
}

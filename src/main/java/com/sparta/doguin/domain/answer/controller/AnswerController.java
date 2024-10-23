package com.sparta.doguin.domain.answer.controller;

import com.sparta.doguin.domain.answer.dto.AnswerRequest;
import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.common.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface AnswerController {
    ResponseEntity<ApiResponse<AnswerResponse.Response>> create(AnswerRequest.Request answerRequest);
    ResponseEntity<ApiResponse<AnswerResponse.Response>> update(long answerId, AnswerRequest.Request answerRequest);
    ResponseEntity<ApiResponse<AnswerResponse.Response>> viewOne(long answerId);
    ResponseEntity<ApiResponse<Page<AnswerResponse.Response>>> viewAll(int page, int size);
    ResponseEntity<ApiResponse<Void>> delete(Long answerId);
}

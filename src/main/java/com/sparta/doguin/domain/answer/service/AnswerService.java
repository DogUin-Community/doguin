package com.sparta.doguin.domain.answer.service;

import com.sparta.doguin.domain.answer.dto.AnswerRequest;
import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.common.response.ApiResponse;
import org.springframework.data.domain.Page;

public interface AnswerService {
    ApiResponse<AnswerResponse.Response> create(AnswerRequest.Request request);
    ApiResponse<AnswerResponse.Response> update(long answerId, AnswerRequest.Request request);
    ApiResponse<AnswerResponse.Response> viewOne(long answerId);
    ApiResponse<Page<AnswerResponse.Response>> viewAll(int page, int size);
    ApiResponse<Void> delete(long answerId);
}

package com.sparta.doguin.domain.answer.controller;

import com.sparta.doguin.domain.answer.dto.AnswerRequest;
import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.service.AnswerService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/answer/question")
public class QuestionAnswerController implements AnswerController {

    @Autowired
    private final AnswerService questionAnswerService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> create(@RequestBody AnswerRequest.Request request) {
        return ApiResponse.of(questionAnswerService.create(request));
    }

    @Override
    @PutMapping("/{answerId}")
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> update(@PathVariable long answerId, @RequestBody AnswerRequest.Request request) {
        return ApiResponse.of(questionAnswerService.update(answerId, request));
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AnswerResponse.Response>>> viewAll(@RequestParam(defaultValue = "1") int page,
                                                                              @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.of(questionAnswerService.viewAll(page, size));
    }

    @Override
    @GetMapping("/{answerId}")
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> viewOne(@PathVariable long answerId) {
        return ApiResponse.of(questionAnswerService.viewOne(answerId));
    }

    @Override
    @DeleteMapping("/{answerId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long answerId) {
        return ApiResponse.of(questionAnswerService.delete(answerId));
    }
}

package com.sparta.doguin.domain.question.controller;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.question.dto.QuestionRequest;
import com.sparta.doguin.domain.question.dto.QuestionResponse;
import com.sparta.doguin.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<ApiResponse<QuestionResponse.CreatedQuestion>> createdQuestion(@RequestBody QuestionRequest.CreatedQuestion request) {
        return ApiResponse.of(questionService.createdQuestion(request));
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<ApiResponse<QuestionResponse.CreatedQuestion>> updatedQuestion(@PathVariable Long questionId,
                                                                                         @RequestBody QuestionRequest.UpdateQuestion request) {
        return ApiResponse.of(questionService.updatedQuestion(questionId, request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<QuestionResponse.GetQuestions>>> getQuestions() {
        return ApiResponse.of(questionService.getQuestions());
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<ApiResponse<QuestionResponse.GetQuestion>> getQuestion(@PathVariable Long questionId) {
        return ApiResponse.of(questionService.getQuestion(questionId));
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable Long questionId) {
        return ApiResponse.of(questionService.deleteQuestion(questionId));
    }
}

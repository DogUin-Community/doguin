package com.sparta.doguin.domain.answer.controller;

import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.answer.dto.AnswerRequest;
import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.service.AnswerService;
import com.sparta.doguin.domain.answer.service.QuestionAnswerService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class QuestionAnswerController {

    private final AnswerService answerService;

    public QuestionAnswerController(QuestionAnswerService answerService) {
        this.answerService = answerService;
    }

    // 질문 답변 생성
    @PostMapping("question/{questionId}/answer")
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> createQuestionAnswer(@AuthenticationPrincipal AuthUser authUser,
                                                                                     @PathVariable Long questionId,
                                                                                     @RequestBody AnswerRequest.Request request) {
        return ApiResponse.of(answerService.createQuestionAnswer(authUser, questionId, request));
    }

    // 질문 답변 수정
    @PutMapping("question/{questionId}/answer/{answerId}")
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> updateQuestionAnswer(@AuthenticationPrincipal AuthUser authUser,
                                                                                     @PathVariable Long questionId,
                                                                                     @PathVariable Long answerId,
                                                                                     @RequestBody AnswerRequest.Request request) {
        return ApiResponse.of(answerService.updateQuestionAnswer(authUser, questionId, answerId, request));
    }

    @GetMapping("question/{questionId}/answer")
    public ResponseEntity<ApiResponse<Page<AnswerResponse.GetResponse>>> viewAllQuestionAnswer(@PathVariable long questionId,
                                                                                            @RequestParam(defaultValue = "1") int page,
                                                                                            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.of(answerService.viewAllQuestionAnswer(questionId, page, size));
    }

    @GetMapping("question/{questionId}/answer/{answerId}")
    public ResponseEntity<ApiResponse<AnswerResponse.GetResponse>> viewOneQuestionAnswer(@PathVariable long questionId,
                                                                                         @PathVariable long answerId) {
        return ApiResponse.of(answerService.viewOneQuestionAnswer(questionId, answerId));
    }

    // 질문 답변 삭제
    @DeleteMapping("question/{questionId}/answer/{answerId}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestionAnswer(@AuthenticationPrincipal AuthUser authUser,
                                                     @PathVariable Long questionId,
                                                     @PathVariable Long answerId) {
        return ApiResponse.of(answerService.deleteQuestionAnswer(authUser, questionId, answerId));
    }

}

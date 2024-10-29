package com.sparta.doguin.domain.answer.controller;

import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.answer.dto.AnswerRequest;
import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.service.AnswerService;
import com.sparta.doguin.domain.answer.service.ApplyAnswerService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class ApplyAnswerController {

    private final AnswerService answerService;

    public ApplyAnswerController(ApplyAnswerService answerService) {
        this.answerService = answerService;
    }

    // 대댓글 생성
    @PostMapping("question/{questionId}/answer/{answerId}")
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> createApplyAnswer (@AuthenticationPrincipal AuthUser authUser,
                                                                                   @PathVariable long questionId,
                                                                                   @PathVariable long answerId,
                                                                                   @RequestBody AnswerRequest.Request request) {
        return ApiResponse.of(answerService.createApplyAnswer(authUser, questionId, answerId, request));
    }

    // 대댓글 수정
    @PutMapping("question/{questionId}/parentAnswer/{parentId}/answer/{answerId}")
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> updateApplyAnswer(@AuthenticationPrincipal AuthUser authUser,
                                                                                  @PathVariable long questionId,
                                                                                  @PathVariable long parentId,
                                                                                  @PathVariable long answerId,
                                                                                  @RequestBody AnswerRequest.Request request) {
        return ApiResponse.of(answerService.updateApplyAnswer(authUser, questionId, parentId, answerId, request));
    }

    // 대댓글 삭제
    @DeleteMapping("question/{questionId}/parentAnswer/{parentId}/answer/{answerId}")
    public ResponseEntity<ApiResponse<Void>> deleteApplyAnswer(@AuthenticationPrincipal AuthUser authUser,
                                                               @PathVariable long questionId,
                                                               @PathVariable long parentId,
                                                               @PathVariable long answerId) {
        return ApiResponse.of(answerService.deleteApplyAnswer(authUser, questionId, parentId, answerId));
    }
}

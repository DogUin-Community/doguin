package com.sparta.doguin.domain.answer.controller;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.answer.dto.AnswerRequest;
import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.service.AnswerService;
import com.sparta.doguin.domain.answer.service.InquiryAnswerService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class InquiryAnswerController {

    private final AnswerService answerService;

    public InquiryAnswerController(InquiryAnswerService answerService) {
        this.answerService = answerService;
    }

    // 1:1 문의 답변 생성
    @PostMapping("inquiries/{boardId}/answer")
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> create (@AuthenticationPrincipal AuthUser authUser,
                                                                        @PathVariable long boardId,
                                                                        @RequestBody AnswerRequest.Request request) {
        return ApiResponse.of(answerService.create(authUser, boardId, request));
    }

    @PutMapping("inquiries/{boardId}/answer/{answerId}")
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> update(@AuthenticationPrincipal AuthUser authUser,
                                                                       @PathVariable long boardId,
                                                                       @PathVariable long answerId,
                                                                       @RequestBody AnswerRequest.Request request) {
        return ApiResponse.of(answerService.update(authUser, boardId, answerId, request));
    }

    @GetMapping("inquiries/{boardId}/answer")
    public ResponseEntity<ApiResponse<Page<AnswerResponse.Response>>> viewAll(@PathVariable long boardId,
                                                                              @RequestParam(defaultValue = "1") int page,
                                                                              @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.of(answerService.viewAll(boardId, page, size));
    }

    @GetMapping("inquiries/{boardId}/answer/{answerId}")
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> viewOne(@PathVariable long boardId,
                                                                        @PathVariable long answerId) {
        return ApiResponse.of(answerService.viewOne(boardId, answerId));
    }

    @DeleteMapping("inquiries/{boardId}/answer/{answerId}")
    public ResponseEntity<ApiResponse<Void>> delete(@AuthenticationPrincipal AuthUser authUser,
                                                    @PathVariable long boardId,
                                                    @PathVariable long answerId) {
        return ApiResponse.of(answerService.delete(authUser, boardId, answerId));
    }
}

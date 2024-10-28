package com.sparta.doguin.domain.answer.controller;

import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.answer.dto.AnswerRequest;
import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.service.AnswerService;
import com.sparta.doguin.domain.answer.service.BulletinAnswerService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class BulletinAnswerController {

    private final AnswerService answerService;

    public BulletinAnswerController(BulletinAnswerService answerService) {
        this.answerService = answerService;
    }

    // 자유게시판 댓글 생성
    @PostMapping("boards/{boardId}/answer")
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> create (@AuthenticationPrincipal AuthUser authUser,
                                                                        @PathVariable long boardId,
                                                                        @RequestBody AnswerRequest.Request request) {
        return ApiResponse.of(answerService.create(authUser, boardId, request));
    }

    // 자유게시판 댓글 수정
    @PutMapping("boards/{boardId}/answer/{answerId}")
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> update(@AuthenticationPrincipal AuthUser authUser,
                                                                       @PathVariable long boardId,
                                                                       @PathVariable long answerId,
                                                                       @RequestBody AnswerRequest.Request request) {
        return ApiResponse.of(answerService.update(authUser, boardId, answerId, request));
    }

    @GetMapping("boards/{boardId}/answer")
    public ResponseEntity<ApiResponse<Page<AnswerResponse.Response>>> viewAll(@PathVariable long boardId,
                                                                              @RequestParam(defaultValue = "1") int page,
                                                                              @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.of(answerService.viewAll(boardId, page, size));
    }

    @GetMapping("boards/{boardId}/answer/{answerId}")
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> viewOne(@PathVariable long boardId,
                                                                        @PathVariable long answerId) {
        return ApiResponse.of(answerService.viewOne(boardId, answerId));
    }

    @DeleteMapping("boards/{boardId}/answer/{answerId}")
    public ResponseEntity<ApiResponse<Void>> delete(@AuthenticationPrincipal AuthUser authUser,
                                                    @PathVariable long boardId,
                                                    @PathVariable long answerId) {
        return ApiResponse.of(answerService.delete(authUser, boardId, answerId));
    }


}

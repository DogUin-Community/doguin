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
@RequestMapping("/api/v1/comment/bulletin")
public class BulletinAnswerController implements AnswerController {

    @Autowired
    private final AnswerService bulletinAnswerService;

    /**
     * 질문 생성
     *
     * @param request
     * @return
     */
    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> create(@RequestBody AnswerRequest.Request request) {
        return ApiResponse.of(bulletinAnswerService.create(request));
    }

    /**
     * 질문 수정
     *
     * @param answerId
     * @param request
     * @return
     */
    @Override
    @PutMapping("/{answerId}")
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> update(@PathVariable long answerId, @RequestBody AnswerRequest.Request request) {
        return ApiResponse.of(bulletinAnswerService.update(answerId, request));
    }

    /**
     * 질문 다건 조회
     *
     * @return
     */
    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AnswerResponse.Response>>> viewAll(@RequestParam(defaultValue = "1") int page,
                                                                              @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.of(bulletinAnswerService.viewAll(page, size));
    }

    /**
     * 질문 단건 조회
     *
     * @param answerId
     * @return
     */
    @Override
    @GetMapping("/{answerId}")
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> viewOne(@PathVariable long answerId) {
        return ApiResponse.of(bulletinAnswerService.viewOne(answerId));
    }

    @Override
    @DeleteMapping("/{answerId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long answerId) {
        return ApiResponse.of(bulletinAnswerService.delete(answerId));
    }

}

package com.sparta.doguin.domain.question.controller;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.question.dto.QuestionRequest;
import com.sparta.doguin.domain.question.dto.QuestionResponse;
import com.sparta.doguin.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/question")
public class QuestionController {

    private final QuestionService questionService;

    /**
     * 질문 생성
     *
     * @param request 질문 생성 시 필요한 정보가 담긴 객체
     * @since 1.0
     * @return 생성 된 질문에 대한 응답 객체
     * @author 유태이
     */
    @PostMapping
    public ResponseEntity<ApiResponse<QuestionResponse.CreatedQuestion>> createdQuestion(@RequestBody QuestionRequest.CreatedQuestion request) {
        return ApiResponse.of(questionService.createdQuestion(request));
    }

    /**
     * 질문 수정
     *
     * @param questionId 수정할 질문의 ID
     * @param request 수정할 질문의 정보가 담긴 객체
     * @since 1.0
     * @return 수정 된 질문에 대한 응답 객체
     * @author 유태이
     */
    @PutMapping("/{questionId}")
    public ResponseEntity<ApiResponse<QuestionResponse.CreatedQuestion>> updatedQuestion(@PathVariable Long questionId,
                                                                                         @RequestBody QuestionRequest.UpdateQuestion request) {
        return ApiResponse.of(questionService.updatedQuestion(questionId, request));
    }

    /**
     * 질문 전체 조회
     *
     * @param page 조회할 페이지 번호(기본 값: 1)
     * @param size 한 페이지에 포함될 질문 수(기본값: 10)
     * @since 1.0
     * @return 요청한 페이지에 해당하는 질문 목록
     * @author 유태이
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<QuestionResponse.GetQuestions>>> getQuestions(@RequestParam(defaultValue = "1")int page,
                                                                                         @RequestParam(defaultValue = "10")int size) {
        return ApiResponse.of(questionService.getQuestions(page, size));
    }

    /**
     * 질문 단건 조회
     *
     * @param questionId 조회할 질문의 ID
     * @since 1.0
     * @return 요청한 질문
     * @author 유태이
     */
    @GetMapping("/{questionId}")
    public ResponseEntity<ApiResponse<QuestionResponse.GetQuestion>> getQuestion(@PathVariable Long questionId) {
        return ApiResponse.of(questionService.getQuestion(questionId));
    }

    /**
     * 질문 삭제
     *
     * @param questionId 삭제할 질문의 ID
     * @since 1.0
     * @return 삭제 결과
     * @author 유태이
     */
    @DeleteMapping("/{questionId}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable Long questionId) {
        return ApiResponse.of(questionService.deleteQuestion(questionId));
    }
}

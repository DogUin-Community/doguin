package com.sparta.doguin.domain.question.controller;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseQuestionEnum;
import com.sparta.doguin.domain.question.dto.QuestionRequest;
import com.sparta.doguin.domain.question.dto.QuestionResponse;
import com.sparta.doguin.domain.question.service.QuestionService;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ApiResponse<QuestionResponse.CreatedQuestion>> createdQuestion(@AuthenticationPrincipal AuthUser authUser,
                                                                                         @RequestBody QuestionRequest.CreatedQuestion request) {
        return ApiResponse.of(questionService.createdQuestion(authUser, request));
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
    public ResponseEntity<ApiResponse<QuestionResponse.CreatedQuestion>> updatedQuestion(@AuthenticationPrincipal AuthUser authUser,
                                                                                         @PathVariable Long questionId,
                                                                                         @RequestBody QuestionRequest.UpdateQuestion request) {
        return ApiResponse.of(questionService.updatedQuestion(authUser, questionId, request));
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
    public ResponseEntity<ApiResponse<QuestionResponse.GetQuestion>> getQuestion(@AuthenticationPrincipal AuthUser authUser,
                                                                                 @PathVariable Long questionId) {
        User user = authUser != null ? User.fromAuthUser(authUser) : null;
        QuestionResponse.GetQuestion response = questionService.getQuestion(user, questionId);
        return ApiResponse.of(ApiResponse.of(ApiResponseQuestionEnum.QUESTION_FIND_ONE_SUCCESS, response));
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
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@AuthenticationPrincipal AuthUser authUser,
                                                            @PathVariable Long questionId) {
        return ApiResponse.of(questionService.deleteQuestion(authUser, questionId));
    }

    /**
     * 질문 검색: 제목 또는 내용의 키워드로 질문을 검색하여 페이징 된 결과 반환
     *
     * @param page 페이지 번호
     * @param size 페이지 당 결과 개수
     * @param title 검색할 질문의 제목(선택)
     * @param content 검색할 질문의 내용(선택)
     * @return 검색된 질문 목록을 페이징하여 반환
     * @since 1.0
     * @author 유태이
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<QuestionResponse.SearchQuestion>>> search(@RequestParam(defaultValue = "0", required = false) int page,
                                                                                     @RequestParam(defaultValue = "10", required = false) int size,
                                                                                     @RequestParam(required = false) String title,
                                                                                     @RequestParam(required = false) String content) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.of(questionService.search(pageable, title, content));
    }

    @GetMapping("/rank")
    public ResponseEntity<ApiResponse<Page<Long>>> viewPopular(@RequestParam(defaultValue = "0", required = false) int page,
                                                               @RequestParam(defaultValue = "5", required = false) int size) {
        Page<Long> response = questionService.viewPopular(page, size);
        return ApiResponse.of(ApiResponse.of(ApiResponseQuestionEnum.QUESTION_RANK_FIND_ALL_SUCCESS, response));
    }


}

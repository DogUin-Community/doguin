package com.sparta.doguin.domain.question.controller;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseQuestionEnum;
import com.sparta.doguin.domain.question.dto.QuestionRequest.QuestionRequestCreate;
import com.sparta.doguin.domain.question.dto.QuestionRequest.QuestionRequestUpdate;
import com.sparta.doguin.domain.question.dto.QuestionResponse;
import com.sparta.doguin.domain.question.service.QuestionService;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/question")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<ApiResponse<QuestionResponse>> createdQuestion(@AuthenticationPrincipal AuthUser authUser,
                                                                         @Valid @RequestPart QuestionRequestCreate request,
                                                                         @RequestPart(value = "file", required = false) List<MultipartFile> files) {
        return ApiResponse.of(questionService.createdQuestion(authUser, request, files));
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<ApiResponse<QuestionResponse>> updatedQuestion(@AuthenticationPrincipal AuthUser authUser,
                                                                         @PathVariable Long questionId,
                                                                         @Valid @RequestPart QuestionRequestUpdate request,
                                                                         @RequestPart(value = "file", required = false) List<MultipartFile> files) {
        return ApiResponse.of(questionService.updatedQuestion(authUser, questionId, request, files));
    }

    @PutMapping("/{questionId}/accept")
    public ResponseEntity<ApiResponse<Void>> acceptQuestion(@AuthenticationPrincipal AuthUser authUser,
                                                            @PathVariable Long questionId) {
        return ApiResponse.of(questionService.acceptQuestion(authUser, questionId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<QuestionResponse.GetQuestions>>> getQuestions(@RequestParam(defaultValue = "1")int page,
                                                                                         @RequestParam(defaultValue = "10")int size) {
        return ApiResponse.of(questionService.getQuestions(page, size));
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<ApiResponse<QuestionResponse.GetQuestion>> getQuestion(@AuthenticationPrincipal AuthUser authUser,
                                                                                 @PathVariable Long questionId) {
        User user = authUser != null ? User.fromAuthUser(authUser) : null;
        QuestionResponse.GetQuestion response = questionService.getQuestion(user, questionId);
        return ApiResponse.of(ApiResponse.of(ApiResponseQuestionEnum.QUESTION_FIND_ONE_SUCCESS, response));
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@AuthenticationPrincipal AuthUser authUser,
                                                            @PathVariable Long questionId) {
        return ApiResponse.of(questionService.deleteQuestion(authUser, questionId));
    }

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

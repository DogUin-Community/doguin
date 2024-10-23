package com.sparta.doguin.domain.answer.controller;

import com.sparta.doguin.domain.answer.dto.AnswerRequest;
import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.service.AnswerService;
import com.sparta.doguin.domain.answer.service.NoticeAnswerService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class NoticeAnswerController implements AnswerController {

    private final AnswerService answerService;
    public NoticeAnswerController(NoticeAnswerService answerService) {
        this.answerService = answerService;
    }

    /**
     * 공지사항 댓글 등록
     *
     * @param request 클라이언트가 요청한 댓글의 정보가 담긴 객체
     * @since 1.0
     * @return 생성 된 댓글의 정보를 포함하는 ApiResponse
     * @author 유태이
     */
    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> create(@RequestBody AnswerRequest.Request request) {
        ApiResponse<AnswerResponse.Response> response = answerService.create(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 공지사항 댓글 수정
     *
     * @param answerId 수정할 댓글 ID
     * @param request 수정할 댓글의 정보가 담긴 객체
     * @since 1.0
     * @return 수정 된 댓글의 정보를 포함하는 ApiResponse
     * @author 유태이
     */
    @Override
    @PutMapping("/{answerId}")
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> update(@PathVariable long answerId, @RequestBody AnswerRequest.Request request) {
        ApiResponse<AnswerResponse.Response> response = answerService.update(answerId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 공지사항 댓글 전체 조회
     *
     * @param page 조회할 페이지 번호(기본 값: 1)
     * @param size 한 페이지에 포함될 질문 수(기본 값 10)
     * @since 1.0
     * @return 요청한 페이지에 해당하는 댓글 목록이 포함 된 ApiResponse
     * @author 유태이
     */
    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AnswerResponse.Response>>> viewAll(@RequestParam(defaultValue = "1") int page,
                                                                              @RequestParam(defaultValue = "10") int size) {
        ApiResponse<Page<AnswerResponse.Response>> response = answerService.viewAll(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * 공지사항 댓글 단건 조회
     *
     * @param answerId 조회할 댓글 ID
     * @since 1.0
     * @return 요청한 댓글 정보가 포함 된 ApiResponse
     * @author 유태이
     */
    @Override
    @GetMapping("/{answerId}")
    public ResponseEntity<ApiResponse<AnswerResponse.Response>> viewOne(@PathVariable long answerId) {
        ApiResponse<AnswerResponse.Response> response = answerService.viewOne(answerId);
        return ResponseEntity.ok(response);
    }

    /**
     * 공지사항 댓글 삭제
     *
     * @param answerId 삭제할 댓글 ID
     * @since 1.0
     * @return 삭제가 성공적으로 완료되었음을 나타내는 ApiResponse
     * @author 유태이
     */
    @Override
    @DeleteMapping("/{answerId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long answerId) {
        ApiResponse<Void> response = answerService.delete(answerId);
        return ResponseEntity.ok(response);
    }
}

package com.sparta.doguin.domain.board.controller;

import com.sparta.doguin.domain.board.dto.request.BoardRequest;
import com.sparta.doguin.domain.board.dto.response.BoardResponse;
import com.sparta.doguin.domain.board.service.BulletinService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseBoardEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/bulletins")
public class BulletinController implements BoardController{

    private final BulletinService bulletinService;

    @Override
    public ResponseEntity<ApiResponse<BoardResponse>> create(BoardRequest boardRequest) {
        BoardResponse response = BoardResponse.from(bulletinService.create(boardRequest));
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.NOTICE_CREATE_SUCCESS, response));
    }

    @Override
    public ResponseEntity<ApiResponse<BoardResponse>> update(Long boardId, BoardRequest boardRequest) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<BoardResponse>> viewOne(Long boardId) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<Page<BoardResponse>>> viewAll(int page, int size) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<Page<BoardResponse>>> search(int page, int size, String title) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> delete(Long boardId) {
        return null;
    }
}

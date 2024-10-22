package com.sparta.doguin.domain.board.controller;

import com.sparta.doguin.domain.board.dto.request.BoardRequest;
import com.sparta.doguin.domain.board.dto.response.BoardResponse;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.common.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.function.EntityResponse;

public interface BoardController {

    ResponseEntity<ApiResponse<BoardResponse>> create(BoardRequest boardRequest);
    ResponseEntity<ApiResponse<BoardResponse>> update(Long boardId,BoardRequest boardRequest);
    ResponseEntity<ApiResponse<BoardResponse>> viewOne(Long boardId);
    ResponseEntity<ApiResponse<Page<BoardResponse>>> viewAll(int page, int size);
    ResponseEntity<ApiResponse<Page<BoardResponse>>> search(int page, int size,String title);
    ResponseEntity<ApiResponse<Void>> delete(Long boardId);
}

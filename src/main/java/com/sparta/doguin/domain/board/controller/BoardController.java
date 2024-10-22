package com.sparta.doguin.domain.board.controller;

import com.sparta.doguin.domain.board.dto.request.BoardRequest;
import com.sparta.doguin.domain.board.dto.response.BoardResponse;
import com.sparta.doguin.domain.board.entity.Board;
import org.springframework.data.domain.Page;

public interface BoardController {
    BoardResponse create(BoardRequest boardRequest);
    BoardResponse update(Long boardId,BoardRequest boardRequest);
    BoardResponse viewOne(Long boardId);
    Page<BoardResponse> viewAll(int page, int size);
    Page<BoardResponse> search(int page, int size,String title);
    void delete(Long boardId);
}

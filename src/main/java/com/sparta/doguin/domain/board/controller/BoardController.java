package com.sparta.doguin.domain.board.controller;

import com.sparta.doguin.domain.board.dto.request.BoardRequest;
import com.sparta.doguin.domain.board.dto.response.BoardResponse;
import com.sparta.doguin.domain.board.entity.Board;
import org.springframework.data.domain.Page;

public interface BoardController {
    BoardResponse create(BoardRequest boardRequest);
    BoardResponse update(Long boardId,BoardRequest boardRequest);
    BoardResponse viewOne();
    Page<Board> viewAll();
    Page<Board> search();
    void delete();
}

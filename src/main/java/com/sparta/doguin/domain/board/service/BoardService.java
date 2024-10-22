package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.board.dto.request.BoardRequest;
import com.sparta.doguin.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {

    Board create(BoardRequest boardRequest);
    Board update(BoardRequest boardRequest);
    Board viewOne(Long boardId);
    Page<Board> viewAll(Pageable pageable);
    Page<Board> search(String title, Pageable pageable);
    void delete(Long bardId);
}

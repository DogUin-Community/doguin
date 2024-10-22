package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.board.BoardType;
import com.sparta.doguin.domain.board.dto.request.BoardRequest;
import com.sparta.doguin.domain.board.dto.response.BoardResponse;
import com.sparta.doguin.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {

    Board create(BoardRequest boardRequest,BoardType boardType);
    Board update(Long boardId, BoardRequest boardRequest);
    Board viewOne(Long boardId);
    Page<BoardResponse> viewAll(int page, int size, BoardType boardType);
    Page<BoardResponse> search(int page,int size,String title,BoardType boardType);
    void delete(Long bardId);
}

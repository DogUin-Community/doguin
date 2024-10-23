package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.board.dto.request.BoardRequest;
import com.sparta.doguin.domain.board.dto.response.BoardResponse;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.user.entity.User;
import org.springframework.data.domain.Page;

public interface BoardService {

    Board create(User user, BoardRequest boardRequest);
    Board update(User user,Long boardId, BoardRequest boardRequest);
    Board viewOne(Long boardId);
    Page<BoardResponse> viewAll(int page, int size);
    Page<BoardResponse> search(int page,int size,String title);
    void delete(User user, Long bardId);
}

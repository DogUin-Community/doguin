package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.board.dto.request.BoardRequest;
import com.sparta.doguin.domain.board.dto.response.BoardResponse;
import com.sparta.doguin.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class BulletinService implements BoardService{
    @Override
    public Board create(BoardRequest boardRequest) {
        return null;
    }

    @Override
    public Board update(Long boardId, BoardRequest boardRequest) {
        return null;
    }

    @Override
    public Board viewOne(Long boardId) {
        return null;
    }

    @Override
    public Page<BoardResponse> viewAll(int page, int size) {
        return null;
    }

    @Override
    public Page<BoardResponse> search(int page, int size, String title) {
        return null;
    }

    @Override
    public void delete(Long bardId) {

    }
}

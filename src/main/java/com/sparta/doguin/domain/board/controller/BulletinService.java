package com.sparta.doguin.domain.board.controller;

import com.sparta.doguin.domain.board.dto.request.BoardRequest;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.board.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/boards/bulletins")
public class BulletinService implements BoardService {
    @Override
    public Board create(BoardRequest boardRequest) {
        return null;
    }

    @Override
    public Board update(BoardRequest boardRequest) {
        return null;
    }

    @Override
    public Board viewOne(Long boardId) {
        return null;
    }

    @Override
    public Page<Board> viewAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Board> search(String title, Pageable pageable) {
        return null;
    }

    @Override
    public void delete(Long bardId) {

    }
}

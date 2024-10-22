package com.sparta.doguin.domain.board.controller;

import com.sparta.doguin.domain.board.dto.request.BoardRequest;
import com.sparta.doguin.domain.board.dto.response.BoardResponse;
import com.sparta.doguin.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/boards/inquiries")
public class InquiryController implements BoardController{
    @Override
    public BoardResponse create(BoardRequest boardRequest) {
        return null;
    }

    @Override
    public BoardResponse update() {
        return null;
    }

    @Override
    public BoardResponse viewOne() {
        return null;
    }

    @Override
    public Page<Board> viewAll() {
        return null;
    }

    @Override
    public Page<Board> search() {
        return null;
    }

    @Override
    public void delete() {

    }
}

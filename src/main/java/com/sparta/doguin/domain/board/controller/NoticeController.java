package com.sparta.doguin.domain.board.controller;

import com.sparta.doguin.domain.board.dto.request.BoardRequest;
import com.sparta.doguin.domain.board.dto.response.BoardResponse;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/notices")
public class NoticeController implements BoardController{

    private final BoardService noticeService;

    @PostMapping
    public BoardResponse create(@RequestBody BoardRequest boardRequest){
        return BoardResponse.from(noticeService.create(boardRequest));
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

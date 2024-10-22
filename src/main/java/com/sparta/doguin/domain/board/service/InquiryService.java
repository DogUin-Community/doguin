package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.board.dto.request.BoardRequest;
import com.sparta.doguin.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class InquiryService implements BoardService{
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

    // 미답변 문의 조회 추가
}

package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.board.BoardType;
import com.sparta.doguin.domain.board.dto.request.BoardRequest;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService implements BoardService{


    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public Board create(BoardRequest boardRequest) {
        Board newNotice = new Board(boardRequest.title(), boardRequest.content(), BoardType.BOARD_NOTICE);
        return boardRepository.save(newNotice);
    }

    @Override
    @Transactional
    public Board update(Long boardId, BoardRequest boardRequest) {
        Board notice = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("없읍니다."));
        notice.update(boardRequest.title(),boardRequest.content());
        return notice;
    }


    @Override
    public Board viewOne(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("없읍니다."));
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

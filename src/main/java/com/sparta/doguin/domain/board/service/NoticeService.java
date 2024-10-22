package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.board.BoardType;
import com.sparta.doguin.domain.board.dto.request.BoardRequest;
import com.sparta.doguin.domain.board.dto.response.BoardResponse;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.board.repository.BoardRepository;
import com.sparta.doguin.domain.common.exception.HandleNotFound;
import com.sparta.doguin.domain.common.response.ApiResponseBoardEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService implements BoardService{


    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public Board create(BoardRequest boardRequest,BoardType boardType) {
        Board board = new Board(boardRequest.title(), boardRequest.content(), boardType);
        return boardRepository.save(board);
    }

    @Override
    @Transactional
    public Board update(Long boardId, BoardRequest boardRequest) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseBoardEnum.NOTICE_NOT_FOUND));
        board.update(boardRequest.title(),boardRequest.content());
        return board;
    }


    @Override
    public Board viewOne(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseBoardEnum.NOTICE_NOT_FOUND));
    }

    @Override
    public Page<BoardResponse> viewAll(int page, int size, BoardType boardType) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Board> boards = boardRepository.findAllByBoardType(pageable,boardType);

        return boards.map(notice -> new BoardResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent()
        ));

    }

    @Override
    public Page<BoardResponse> search(int page,int size,String title, BoardType boardType) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Board> boards = boardRepository.findAllByTitleAndBoardType(pageable,title,boardType);

        return boards.map(notice -> new BoardResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent()
        ));
    }

    @Override
    @Transactional
    public void delete(Long boardId) {

        Board board =boardRepository.findById(boardId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseBoardEnum.NOTICE_NOT_FOUND));

        boardRepository.delete(board);
    }
}

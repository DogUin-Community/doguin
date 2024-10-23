package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.board.BoardType;
import com.sparta.doguin.domain.board.dto.BoardRequest;
import com.sparta.doguin.domain.board.dto.BoardRequest.BoardCommonRequest;
import com.sparta.doguin.domain.board.dto.BoardResponse.BoardCommonResponse;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.board.repository.BoardRepository;
import com.sparta.doguin.domain.common.exception.HandleNotFound;
import com.sparta.doguin.domain.common.exception.InvalidRequestException;
import com.sparta.doguin.domain.common.response.ApiResponseBoardEnum;
import com.sparta.doguin.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InquiryService implements BoardService{

    private final BoardRepository boardRepository;
    private final BoardType boardType = BoardType.BOARD_INQUIRY;

    @Override
    @Transactional
    public Board create(User user, BoardCommonRequest boardRequest) {
        Board board = new Board(boardRequest.title(), boardRequest.content(), boardType,user);
        return boardRepository.save(board);
    }

    @Override
    @Transactional
    public Board update(User user,Long boardId, BoardCommonRequest boardRequest) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseBoardEnum.INQUIRY_NOT_FOUND));
        if(!board.getUser().getId().equals(user.getId())){
            throw new InvalidRequestException(ApiResponseBoardEnum.USER_WRONG);
        }
        if(board.getBoardType()!=boardType){
            throw new InvalidRequestException(ApiResponseBoardEnum.INQUIRY_WRONG);
        }
        board.update(boardRequest.title(),boardRequest.content());
        return board;
    }


    @Override
    public Board viewOneWithUser(Long boardId,User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseBoardEnum.INQUIRY_NOT_FOUND));
        if(!board.getUser().getId().equals(user.getId())){
            throw new InvalidRequestException(ApiResponseBoardEnum.USER_WRONG);
        }
        if (board.getBoardType() != boardType) {
            throw new InvalidRequestException(ApiResponseBoardEnum.INQUIRY_WRONG);
        }
        return  board;
    }

    @Override
    public Page<BoardCommonResponse> viewAllWithUser(int page, int size, User user) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Board> boards = boardRepository.findAllByBoardTypeAndUser(pageable,boardType,user);

        return boards.map(notice -> new BoardCommonResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent()
        ));

    }

    @Override
    public Page<BoardCommonResponse> searchWithUser(int page,int size,String title,User user) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Board> boards = boardRepository.findAllByTitleAndBoardTypeAndUser(pageable,title,boardType,user);

        return boards.map(notice -> new BoardCommonResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent()
        ));
    }

    @Override
    @Transactional
    public void delete(User user, Long boardId) {

        Board board =boardRepository.findById(boardId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseBoardEnum.INQUIRY_NOT_FOUND));
        if(!board.getUser().getId().equals(user.getId())){
            throw new InvalidRequestException(ApiResponseBoardEnum.USER_WRONG);
        }
        if(board.getBoardType()!=boardType){
            throw new InvalidRequestException(ApiResponseBoardEnum.INQUIRY_WRONG);
        }

        boardRepository.delete(board);
    }
}

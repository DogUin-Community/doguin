package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.service.InquiryAnswerService;
import com.sparta.doguin.domain.board.BoardType;
import com.sparta.doguin.domain.board.dto.BoardRequest.BoardCommonRequest;
import com.sparta.doguin.domain.board.dto.BoardResponse;
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

    private final InquiryAnswerService inquiryAnswerService;
    private final BoardRepository boardRepository;
    private final BoardType boardType = BoardType.BOARD_INQUIRY;

    /**
     * 문의 게시물 생성
     * @param user 로그인한 유저
     * @param boardRequest 문의 게시물의 정보 (제목, 내용 등)
     * @since 1.0
     * @return 생성된 문의 게시물 객체
     * @author 김창민
     */
    @Override
    @Transactional
    public Board create(User user, BoardCommonRequest boardRequest) {
        Board board = new Board(boardRequest.title(), boardRequest.content(), boardType,user);
        return boardRepository.save(board);
    }

    /**
     * 문의 게시물 수정
     *
     * @param user 로그인한 유저
     * @param boardId 작성한 문의 id
     * @param boardRequest 문의 게시물의 수정 정보 (제목, 내용 등)
     * @since 1.0
     * @return 수정된 문의 게시물 객체
     * @author 김창민
     */
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

    /**
     * 등록한 문의 게시물 단건 조회
     *
     * @param boardId 조회 문의 일반 게시물의 id
     * @param user 문의를 등록한 유저
     * @since 1.0
     * @return 조회된 문의 게시물 객체
     * @author 김창민
     */
    @Override
    public BoardResponse.BoardWithAnswer viewOneWithUser(Long boardId, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseBoardEnum.INQUIRY_NOT_FOUND));
        if(!board.getUser().getId().equals(user.getId())){
            throw new InvalidRequestException(ApiResponseBoardEnum.USER_WRONG);
        }
        if (board.getBoardType() != boardType) {
            throw new InvalidRequestException(ApiResponseBoardEnum.INQUIRY_WRONG);
        }
        Page<AnswerResponse.Response> responses = inquiryAnswerService.findByBoardId(boardId,PageRequest.of(0,10));

        return new BoardResponse.BoardWithAnswer(board.getId(),board.getTitle(),board.getContent(), responses);
    }

    /**
     * 등록한 문의 게시물 전체 조회
     *
     * @param page 페이지 번호
     * @param size 한 페이지당 게시물 개수
     * @param user 문의를 등록한 유저
     * @since 1.0
     * @return 조회된 한 페이지 내의 모든 게시물
     * @author 김창민
     */
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

    /**
     * 문의 게시물 검색 조회
     *
     * @param page 페이지 번호
     * @param size 한 페이지당 게시물 개수
     * @param title 조회 대상 검색물의 제목
     * @param user 문의를 등록한 유저
     * @since 1.0
     * @return 조회된 게시물의 id, 제목, 내용
     * @author 김창민
     */
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

    /**
     * 문의 게시물 삭제
     *
     * @param user 문의를 등록한 유저
     * @param boardId 삭제 대상 문의 게시물의 id
     * @since 1.0
     * @author 김창민
     */
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

    @Override
    public Board findByUserId(Long userId) {
        return boardRepository.findByUserId(userId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseBoardEnum.INQUIRY_NOT_FOUND));
    }
}

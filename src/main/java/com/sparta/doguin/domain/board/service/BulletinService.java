package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.service.BulletinAnswerService;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class BulletinService implements BoardService {

    private final BoardRepository boardRepository;
    private final BulletinAnswerService bulletinAnswerService;
    private final BoardType boardType = BoardType.BOARD_BULLETIN;

    /**
     * 일반 게시물 생성
     *
     * @param boardRequest 일반 게시물의 정보 (제목, 내용 등)
     * @since 1.0
     * @return 생성된 일반 게시물 객체
     * @author 김창민
     */
    @Override
    @Transactional
    public Board create(User user, BoardCommonRequest boardRequest) {
        Board board = new Board(boardRequest.title(), boardRequest.content(), boardType, user);
        return boardRepository.save(board);
    }

    /**
     * 일반 게시물 수정
     *
     * @param user 로그인 유저
     * @param boardId 작성한 게시물 id
     * @param boardRequest 일반 게시물의 수정 정보 (제목, 내용 등)
     * @since 1.0
     * @throws HandleNotFound 일반 게시물 조회 시 데이터가 없을 경우 발생
     * @throws InvalidRequestException 게시물 제작자와 로그인한 유저가 다를 경우 발생
     * @throws InvalidRequestException 게시물 타입이 일반 게시물이 아닐 경우 발생
     * @return 수정된 일반 게시물 객체
     * @author 김창민
     */
    @Override
    @Transactional
    public Board update(User user,Long boardId, BoardCommonRequest boardRequest) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseBoardEnum.BULLETIN_NOT_FOUND));

        if(!board.getUser().getId().equals(user.getId())){
            throw new InvalidRequestException(ApiResponseBoardEnum.USER_WRONG);
        }
        if (board.getBoardType() != boardType) {
            throw new InvalidRequestException(ApiResponseBoardEnum.BULLETIN_WRONG);
        }
        board.update(boardRequest.title(), boardRequest.content()); // 업데이트 정보 null 처리
        return board;
    }

    /**
     * 일반 게시물 단건 조회
     *
     * @param boardId 조회 대상 일반 게시물의 id
     * @return 조회된 일반 게시물 객체
     * @throws HandleNotFound          일반 게시물 조회 시 데이터가 없을 경우 발생
     * @throws InvalidRequestException 게시물 타입이 일반 게시물이 아닐 경우 발생
     * @author 김창민
     * @since 1.0
     */
    @Override
    public BoardResponse.BoardWithAnswer viewOne(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseBoardEnum.BULLETIN_NOT_FOUND));
        if (board.getBoardType() != boardType) {
            throw new InvalidRequestException(ApiResponseBoardEnum.BULLETIN_WRONG);
        }

        Page<AnswerResponse.Response> responses = bulletinAnswerService.findByBoardId(boardId,PageRequest.of(0,10));

        return new BoardResponse.BoardWithAnswer(board.getId(),board.getTitle(),board.getContent(), responses);
    }

    /**
     * 일반 게시물 전체 조회
     *
     * @param page 페이지 번호
     * @param size 한 페이지당 게시물 개수
     * @since 1.0
     * @return 조회된 한 페이지 내의 모든 게시물
     * @author 김창민
     */
    @Override
    public Page<BoardCommonResponse> viewAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Board> boards = boardRepository.findAllByBoardType(pageable, boardType);

        return boards.map(notice -> new BoardCommonResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent()
        ));
    }

    /**
     * 일반 게시물 검색 조회
     *
     * @param page 페이지 번호
     * @param size 한 페이지당 게시물 개수
     * @param title 조회 대상 검색물의 제목
     * @since 1.0
     * @return 조회된 게시물의 id, 제목, 내용
     * @author 김창민
     */
    @Override
    public Page<BoardCommonResponse> search(int page, int size, String title) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Board> boards = boardRepository.findAllByTitleAndBoardType(pageable, title, boardType);

        return boards.map(notice -> new BoardCommonResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent()
        ));
    }

    /**
     * 일반 게시물 삭제
     *
     * @param user 로그인한 유저
     * @param boardId 삭제 대상 일반 게시물의 id
     * @since 1.0
     * @throws HandleNotFound 일반 게시물 조회 시 데이터가 없을 경우 발생
     * @throws InvalidRequestException 게시물 제작자와 로그인한 유저가 다를 경우 발생
     * @throws InvalidRequestException 게시물 타입이 일반 게시물이 아닐 경우 발생
     * @author 김창민
     */
    @Override
    @Transactional
    public void delete(User user,Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseBoardEnum.BULLETIN_NOT_FOUND));

        if(!board.getUser().getId().equals(user.getId())){
            throw new InvalidRequestException(ApiResponseBoardEnum.USER_WRONG);
        }
        if (board.getBoardType() != boardType) {
            throw new InvalidRequestException(ApiResponseBoardEnum.BULLETIN_WRONG);
        }

        boardRepository.delete(board);
    }

    @Override
    public Page<Board> findByUserId(Long userId) {
        Pageable pageable = PageRequest.of( 0, 10);
        return boardRepository.findAllByBoardTypeAndUserId(pageable,boardType,userId);
    }
}

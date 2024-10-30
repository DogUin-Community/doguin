package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.service.NoticeAnswerService;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService implements BoardService{

    private final BoardRepository boardRepository;
    private final NoticeAnswerService noticeAnswerService;
    private final ViewTrackingService viewTrackingService;

    private final BoardType boardType = BoardType.BOARD_NOTICE;
    private final static String NOTICE_CACHE = "boardNotice";

    /**
     * 공지 게시물 생성
     * @param user 로그인한 admin 유저
     * @param boardRequest 이벤트 게시물의 정보 (제목, 내용 등)
     * @since 1.0
     * @return 생성된 공지 게시물 객체
     * @author 김창민
     */
    @Override
    @Transactional
    public Board create(User user, BoardCommonRequest boardRequest) {
        Board board = new Board(boardRequest.title(), boardRequest.content(), boardType,user);
        return boardRepository.save(board);
    }

    /**
     * 공지 게시물 수정
     *
     * @param user 로그인한 admin 유저
     * @param boardId 작성한 공지 id
     * @param boardRequest 공지 게시물의 수정 정보 (제목, 내용 등)
     * @since 1.0
     * @throws HandleNotFound 공지 게시물 조회 시 데이터가 없을 경우 발생
     * @throws InvalidRequestException 게시물 제작자와 로그인한 유저가 다를 경우 발생
     * @throws InvalidRequestException 게시물 타입이 공지 게시물이 아닐 경우 발생
     * @return 수정된 공지 게시물 객체
     * @author 김창민
     */
    @Override
    @Transactional
    public Board update(User user,Long boardId, BoardCommonRequest boardRequest) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseBoardEnum.NOTICE_NOT_FOUND));
        if(!board.getUser().getId().equals(user.getId())){
            throw new InvalidRequestException(ApiResponseBoardEnum.USER_WRONG);
        }
        if(board.getBoardType()!=boardType){
            throw new InvalidRequestException(ApiResponseBoardEnum.NOTICE_WRONG);
        }
        board.update(boardRequest.title(),boardRequest.content());
        return board;
    }

    /**
     * 공지 게시물 단건 조회
     *
     * @param boardId 조회 대상 공지 게시물의 id
     * @param user 로그인 한 계정 (로그인 안할 수 있음)
     * @return 조회된 공지 게시물 객체
     * @throws HandleNotFound          공지 게시물 조회 시 데이터가 없을 경우 발생
     * @throws InvalidRequestException 게시물 타입이 공지 게시물이 아닐 경우 발생
     * @author 김창민
     * @since 1.0
     */
    @Override
    @Cacheable(value = NOTICE_CACHE,key = "#boardId")
    public BoardResponse.BoardWithAnswer viewOneWithUser(Long boardId, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseBoardEnum.NOTICE_NOT_FOUND));
        if (board.getBoardType() != boardType) {
            throw new InvalidRequestException(ApiResponseBoardEnum.NOTICE_WRONG);
        }
        Page<AnswerResponse.Response> responses = noticeAnswerService.findByBoardId(boardId,PageRequest.of(0,10));

        if(user!=null){
            viewTrackingService.trackUserView(boardId, user.getId());
        }

        Long viewCount =viewTrackingService.getDailyUniqueViewCount(boardId)+board.getView();
        return new BoardResponse.BoardWithAnswer(board.getId(),board.getTitle(),board.getContent(),viewCount, responses);
    }

    /**
     * 공지 게시물 전체 조회
     *
     * @param page 페이지 번호
     * @param size 한 페이지당 게시물 개수
     * @since 1.0
     * @return 조회된 한 페이지 내의 모든 공지 게시물
     * @author 김창민
     */
    @Override
    public Page<BoardCommonResponse> viewAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Board> boards = boardRepository.findAllByBoardType(pageable,boardType);

        return boards.map(notice -> new BoardCommonResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent()
        ));
    }

    /**
     * 공지 게시물 검색 조회
     *
     * @param page 페이지 번호
     * @param size 한 페이지당 게시물 개수
     * @param title 조회 대상 공지의 제목
     * @since 1.0
     * @return 조회된 공지의 id, 제목, 내용
     * @author 김창민
     */
    @Override
    public Page<BoardCommonResponse> search(int page,int size,String title) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Board> boards = boardRepository.findAllByTitleAndBoardType(pageable,title,boardType);

        return boards.map(notice -> new BoardCommonResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent()
        ));
    }

    /**
     * 공지 게시물 삭제
     *
     * @param user 로그인한 admin 유저
     * @param boardId 삭제 대상 공지 게시물의 id
     * @since 1.0
     * @throws HandleNotFound 공지 게시물 조회 시 데이터가 없을 경우 발생
     * @throws InvalidRequestException 게시물 제작자와 로그인한 유저가 다를 경우 발생
     * @throws InvalidRequestException 게시물 타입이 공지 게시물이 아닐 경우 발생
     * @author 김창민
     */
    @Override
    @Transactional
    public void delete(User user, Long boardId) {

        Board board =boardRepository.findById(boardId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseBoardEnum.NOTICE_NOT_FOUND));
        if(!board.getUser().getId().equals(user.getId())){
            throw new InvalidRequestException(ApiResponseBoardEnum.USER_WRONG);
        }
        if(board.getBoardType()!=boardType){
            throw new InvalidRequestException(ApiResponseBoardEnum.NOTICE_WRONG);
        }
        boardRepository.delete(board);
    }

    @Override
    public Page<Board> findByUserId(Long userId) {
        Pageable pageable = PageRequest.of( 0, 10);
        return boardRepository.findAllByBoardTypeAndUserId(pageable,boardType,userId);
    }
}

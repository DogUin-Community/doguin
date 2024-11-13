package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.service.BulletinAnswerService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentDeleteService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentGetService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUpdateService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUploadService;
import com.sparta.doguin.domain.board.BoardType;
import com.sparta.doguin.domain.board.dto.BoardRequest.BoardCommonRequest;
import com.sparta.doguin.domain.board.dto.BoardResponse;
import com.sparta.doguin.domain.board.dto.BoardResponse.BoardCommonResponse;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.board.event.ViewEvent;
import com.sparta.doguin.domain.board.repository.BoardRepository;
import com.sparta.doguin.domain.common.exception.HandleNotFound;
import com.sparta.doguin.domain.common.exception.InvalidRequestException;
import com.sparta.doguin.domain.common.response.ApiResponseBoardEnum;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.notification.slack.SlackEventClass;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sparta.doguin.domain.attachment.constans.AttachmentTargetType.BULLETIN;
import static com.sparta.doguin.domain.attachment.constans.AttachmentTargetType.OUTSOURCING;

@Service
@RequiredArgsConstructor
public class BulletinService implements BoardService {

    private static final Logger log = LoggerFactory.getLogger(BulletinService.class);
    private final BoardRepository boardRepository;
    private final BulletinAnswerService bulletinAnswerService;
    private final PopularService popularService;
    private final ApplicationEventPublisher publisher;

    private final AttachmentUploadService attachmentUploadService;
    private final AttachmentUpdateService attachmentUpdateService;
    private final AttachmentGetService attachmentGetService;
    private final AttachmentDeleteService attachmentDeleteService;

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
    public void create(User user, BoardCommonRequest boardRequest,List<MultipartFile> files) {
        Board board = new Board(boardRequest.title(), boardRequest.content(), boardType, user);
        boardRepository.save(board);

        if(files!=null){
            attachmentUploadService.upload(files,user,board.getId(), BULLETIN);
            attachmentGetService.getFileIds(user.getId(),board.getId(), BULLETIN);
        }
        publisher.publishEvent(new SlackEventClass(user.getId(), user.getNickname(), "(이)가 새 게시물을 등록했습니다."));
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
     * @author 김창민
     */
    @Override
    @Transactional
    public void update(User user,Long boardId, BoardCommonRequest boardRequest,List<MultipartFile> files) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseBoardEnum.BULLETIN_NOT_FOUND));

        if(!board.getUser().getId().equals(user.getId())){
            throw new InvalidRequestException(ApiResponseBoardEnum.USER_WRONG);
        }
        if (board.getBoardType() != boardType) {
            throw new InvalidRequestException(ApiResponseBoardEnum.BULLETIN_WRONG);
        }
        board.update(boardRequest.title(), boardRequest.content()); // 업데이트 정보 null 처리

    }

    /**
     * 일반 게시물 단건 조회
     *
     * @param boardId 조회 대상 일반 게시물의 id
     * @param user 로그인 한 계정 (로그인 안할 수 있음)
     * @return 조회된 일반 게시물과 해당 댓글 객체
     * @throws HandleNotFound          일반 게시물 조회 시 데이터가 없을 경우 발생
     * @throws InvalidRequestException 게시물 타입이 일반 게시물이 아닐 경우 발생
     * @author 김창민
     * @since 1.0
     */
    @Override
    public BoardResponse.BoardWithAnswer viewOneWithUser(Long boardId, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseBoardEnum.BULLETIN_NOT_FOUND));
        if (board.getBoardType() != boardType) {
            throw new InvalidRequestException(ApiResponseBoardEnum.BULLETIN_WRONG);
        }

        List<String> filePaths = attachmentGetService.getAllAttachmentPath(boardId, BULLETIN);

        Page<AnswerResponse.Response> responses = bulletinAnswerService.findByBoardId(boardId,PageRequest.of(0,10));

        publisher.publishEvent(new ViewEvent(boardId, user != null ? user.getId() : null));

        Long viewCount = popularService.getHourUniqueViewCount(boardId)+board.getView(); // 한시간 조회수 + 누적 조회수 로 토탈 조회수

        return new BoardResponse.BoardWithAnswer(board.getId(),board.getTitle(),board.getContent(),viewCount, responses,filePaths);
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
                notice.getTitle()
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
                notice.getTitle()
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

        List<Long> fileIds = attachmentGetService.getFileIds(user.getId(),board.getId(), BULLETIN);
        attachmentDeleteService.delete(user,fileIds);

        boardRepository.delete(board);
    }

    @Override
    public Page<Board> findByUserId(Long userId) {
        Pageable pageable = PageRequest.of( 0, 10);
        return boardRepository.findAllByBoardTypeAndUserId(pageable,boardType,userId);
    }

    /**
     * 인기 게시물 페이지 조회
     *
     * @param page
     * @param size
     * @since 1.0
     * @author 김창민
     */
    public Page<Long> viewPopular(int page, int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        Set<Long> popularBoardIds = popularService.viewPopularBoardList();

        // 게시물 ID 목록이 없을 경우 빈 페이지 반환
        if (popularBoardIds == null || popularBoardIds.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        // 인기 게시물 ID를 리스트로 변환
        List<Long> popularBoardIdList = popularBoardIds.stream().collect(Collectors.toList());
        log.info(String.valueOf(popularBoardIdList.size()));

        // 페이지 처리
        int start = Math.min((page-1) * size, popularBoardIdList.size());
        int end = Math.min(start + size, popularBoardIdList.size());
        List<Long> paginatedBoardIds = popularBoardIdList.subList(start, end);

        return new PageImpl<>(paginatedBoardIds,pageable, popularBoardIdList.size());
    }
}

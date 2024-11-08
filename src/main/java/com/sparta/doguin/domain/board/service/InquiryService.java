package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.service.InquiryAnswerService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentDeleteService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentGetService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUpdateService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUploadService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.sparta.doguin.domain.attachment.constans.AttachmentTargetType.*;

@Service
@RequiredArgsConstructor
public class InquiryService implements BoardService{

    private final InquiryAnswerService inquiryAnswerService;
    private final BoardRepository boardRepository;
    private final PopularService popularService;

    private final AttachmentUploadService attachmentUploadService;
    private final AttachmentUpdateService attachmentUpdateService;
    private final AttachmentGetService attachmentGetService;
    private final AttachmentDeleteService attachmentDeleteService;


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
    public void create(User user, BoardCommonRequest boardRequest, List<MultipartFile> files) {
        Board board = new Board(boardRequest.title(), boardRequest.content(), boardType,user);
        boardRepository.save(board);

        if(files!=null){
            attachmentUploadService.upload(files,user,board.getId(), INQUIRY);
            attachmentGetService.getFileIds(user.getId(),board.getId(), INQUIRY);
        }

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
    public void update(User user,Long boardId, BoardCommonRequest boardRequest,List<MultipartFile> files) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseBoardEnum.INQUIRY_NOT_FOUND));
        if(!board.getUser().getId().equals(user.getId())){
            throw new InvalidRequestException(ApiResponseBoardEnum.USER_WRONG);
        }
        if(board.getBoardType()!=boardType){
            throw new InvalidRequestException(ApiResponseBoardEnum.INQUIRY_WRONG);
        }
        board.update(boardRequest.title(),boardRequest.content());
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
        List<String> filePaths = attachmentGetService.getAllAttachmentPath(boardId, INQUIRY);
        Page<AnswerResponse.Response> responses = inquiryAnswerService.findByBoardId(boardId,PageRequest.of(0,10));
        popularService.trackUserView(boardId, user.getId());
        Long viewCount = popularService.getHourUniqueViewCount(boardId)+board.getView();

        return new BoardResponse.BoardWithAnswer(board.getId(),board.getTitle(),board.getContent(),viewCount, responses,filePaths);
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

        Page<Board> boards = boardRepository.findAllByBoardTypeAndUserId(pageable,boardType,user.getId());

        return boards.map(notice -> new BoardCommonResponse(
                notice.getId(),
                notice.getTitle()
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
                notice.getTitle()
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
    public Page<Board> findByUserId(Long userId) {
        Pageable pageable = PageRequest.of( 0, 10);
        return boardRepository.findAllByBoardTypeAndUserId(pageable,boardType,userId);
    }
}

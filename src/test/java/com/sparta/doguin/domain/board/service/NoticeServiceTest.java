package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.board.BoardType;
import com.sparta.doguin.domain.board.dto.BoardRequest;
import com.sparta.doguin.domain.board.dto.BoardResponse;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.board.repository.BoardRepository;
import com.sparta.doguin.domain.common.exception.InvalidRequestException;
import com.sparta.doguin.domain.common.response.ApiResponseBoardEnum;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Transactional
@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private NoticeService noticeService;

    private User admin;
    private User user;
    private Board board;

    @BeforeEach
    void setUp() {
        admin = new User(1L, "admin@gmail.com", "AAAaaa111!!!", "관리자입니다.", UserType.INDIVIDUAL, UserRole.ROLE_ADMIN);
        user = new User(2L, "user@gmail.com", "AAAaaa111!!!", "유저입니다.", UserType.INDIVIDUAL, UserRole.ROLE_USER);

        board = new Board("공지 게시물", "공지 게시물", BoardType.BOARD_NOTICE,admin);
        ReflectionTestUtils.setField(board,"id",1L);
    }

    @Test
    @DisplayName("공지 게시물 등록 성공 테스트")
    void create() {
        BoardRequest.BoardCommonRequest boardCommonRequest = new BoardRequest.BoardCommonRequest("공지 게시물","공지 게시물");
        given(boardRepository.save(any(Board.class))).willReturn(board);

        Board result = noticeService.create(admin, boardCommonRequest);

        assertEquals(result, board);
    }

    @Test
    @DisplayName("공지 게시물 수정 성공 테스트")
    void update() {
        BoardRequest.BoardCommonRequest boardCommonRequest = new BoardRequest.BoardCommonRequest("수정된 공지 게시물","수정된 공지 게시물");
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        Board result = noticeService.update(admin, 1L,boardCommonRequest);

        assertEquals(result, board);

    }
    @Test
    @DisplayName("공지 게시물 수정 실패 테스트(등록자 다름)")
    void update_등록자_다름() {
        User user1 = new User(2L, "user1@gmail.com", "AAAaaa111!!!", "다른 유저 입니다.", UserType.INDIVIDUAL, UserRole.ROLE_ADMIN);
        BoardRequest.BoardCommonRequest boardCommonRequest = new BoardRequest.BoardCommonRequest("수정된 공지 게시물","수정된 공지 게시물");
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        // When
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                noticeService.update(user1, 1L, boardCommonRequest)
        );

        // Then
        assertEquals(exception.getApiResponseEnum().getMessage(), ApiResponseBoardEnum.USER_WRONG.getMessage());

    }
    @Test
    @DisplayName("일반 게시물 수정 실패 테스트(게시물 타입 다름)")
    void update_게시물_타입_다름() {
        BoardRequest.BoardCommonRequest boardCommonRequest = new BoardRequest.BoardCommonRequest("수정된 이벤트 게시물","수정된 이벤트 게시물");
        Board board1 = new Board("일반 게시물", "일반 게시물", BoardType.BOARD_BULLETIN,admin);
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board1));

        // When
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                noticeService.update(admin, 1L, boardCommonRequest)
        );

        // Then
        assertEquals(exception.getApiResponseEnum().getMessage(), ApiResponseBoardEnum.NOTICE_WRONG.getMessage());

    }

    @Test
    @DisplayName("공지 게시물 단일 조회 성공 테스트")
    void viewOne() {
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        Board result = noticeService.viewOne( 1L);
        assertEquals(result, board);
    }

    @Test
    @DisplayName("공지 게시물 단일 조회 살패 테스트(게시물 타입 다름)")
    void viewOne_타입_다름() {
        Board board1 = new Board("일반 게시물", "일반 게시물", BoardType.BOARD_BULLETIN,admin);
        ReflectionTestUtils.setField(board1,"id",2L);
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board1));

        // When
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                noticeService.viewOne(2L )
        );

        // Then
        assertEquals(exception.getApiResponseEnum().getMessage(), ApiResponseBoardEnum.NOTICE_WRONG.getMessage());

    }

    @Test
    @DisplayName("공지 게시물 전체 조회 성공 테스트")
    void viewAll() {
        Board board2 = new Board("공지 게시물2", "공지 게시물2", BoardType.BOARD_NOTICE,admin);
        ReflectionTestUtils.setField(board2,"id",2L);
        Board board3 = new Board("공지 게시물3", "공지 게시물3", BoardType.BOARD_NOTICE,admin);
        ReflectionTestUtils.setField(board3,"id",3L);
        List<Board> mockBoards = Arrays.asList(
                board2,board3
        );

        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Board> boardPage = new PageImpl<>(mockBoards, pageable, mockBoards.size());

        given(boardRepository.findAllByBoardType(pageable, BoardType.BOARD_NOTICE)).willReturn(boardPage);

        // when
        Page<BoardResponse.BoardCommonResponse> responsePage = noticeService.viewAll(page, size);

        assertThat(responsePage.getContent().size()).isEqualTo(2);
        assertThat(responsePage.getContent().get(0).title()).isEqualTo("공지 게시물2");
    }

    @Test
    @DisplayName("공지 게시물 검색 성공 테스트")
    void search() {
        Board board2 = new Board("공지 게시물2", "공지 게시물2", BoardType.BOARD_NOTICE,admin);
        ReflectionTestUtils.setField(board2,"id",2L);
        Board board3 = new Board("공지 게시물3", "공지 게시물3", BoardType.BOARD_NOTICE,admin);
        ReflectionTestUtils.setField(board3,"id",3L);
        List<Board> mockBoards = Arrays.asList(
                board2,board3
        );

        int page = 1;
        int size = 2;
        String title = "공지";
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Board> boardPage = new PageImpl<>(mockBoards, pageable, mockBoards.size());

        given(boardRepository.findAllByTitleAndBoardType(pageable, title, BoardType.BOARD_NOTICE)).willReturn(boardPage);

        // When
        Page<BoardResponse.BoardCommonResponse> responsePage = noticeService.search(page, size,title);

        // Then
        assertThat(responsePage.getContent().size()).isEqualTo(2);
        assertThat(responsePage.getContent().get(0).title()).isEqualTo("공지 게시물2");

        verify(boardRepository, times(1)).findAllByTitleAndBoardType(pageable, title,BoardType.BOARD_NOTICE);
    }


    @Test
    @DisplayName("공지 게시물 삭제 성공 테스트")
    void delete() {
        Long boardId = 1L;

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        // When
        noticeService.delete(admin, boardId);

        // Then
        verify(boardRepository, times(1)).delete(board);

    }

    @Test
    @DisplayName("공지 게시물 삭제 실패 테스트(등록자 다름)")
    void delete_등록자_다름() {
        Long boardId = 1L;
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        // When
        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> noticeService.delete(user, boardId));

        // Then
        assertEquals(exception.getApiResponseEnum().getMessage(), ApiResponseBoardEnum.USER_WRONG.getMessage());

    }

    @Test
    @DisplayName("일반 게시물 삭제 실패 테스트(게시물 타입 다름)")
    void delete_게시물_타입_다름() {
        Board board1 = new Board("일반 게시물", "일반 게시물", BoardType.BOARD_BULLETIN,admin);
        ReflectionTestUtils.setField(board1,"id",2L);
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board1));
        Long boardId = 2L;

        // When
        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> noticeService.delete(admin, boardId));

        // Then
        assertEquals(exception.getApiResponseEnum().getMessage(), ApiResponseBoardEnum.NOTICE_WRONG.getMessage());


    }


}
package com.sparta.doguin.domain.answer.service;

import com.sparta.doguin.domain.answer.dto.AnswerRequest;
import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.entity.Answer;
import com.sparta.doguin.domain.answer.repository.AnswerRepository;
import com.sparta.doguin.domain.board.BoardType;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.setup.DataUtil;
import com.sparta.doguin.security.AuthUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
public class NoticeAnswerServiceTest {

    @Mock
    private AnswerRepository answerRepository;


    @InjectMocks
    private NoticeAnswerService answerService;

    @Test
    void 댓글_생성_성공() {
        // given
        AuthUser authUser = DataUtil.authUser1();
        long boardId = DataUtil.one();
        Board board = DataUtil.bulletin1();
        AnswerRequest.Request request = new AnswerRequest.Request("댓글 내용");

        // Board의 ID와 타입을 설정
        setField(board, "id", boardId);
        setField(board, "boardType", BoardType.BOARD_NOTICE);

        // Mock 설정
        given(answerRepository.findBoardById(boardId)).willReturn(board);

        // when
        ApiResponse<AnswerResponse.Response> response = answerService.create(authUser, boardId, request);

        // then
        assertEquals("댓글 등록에 성공하였습니다.", response.getMessage());
    }

    @Test
    void 댓글_수정_성공() {
        // given
        AuthUser authUser = DataUtil.authUser1();
        long boardId = DataUtil.one();
        long answerId = DataUtil.two();
        Board board = DataUtil.bulletin1();
        Answer answer = new Answer("기존 댓글 내용", DataUtil.user1(), board);
        AnswerRequest.Request request = new AnswerRequest.Request("수정된 댓글 내용");

        // Board의 ID와 타입을 설정
        setField(board, "id", boardId);
        setField(board, "boardType", BoardType.BOARD_NOTICE);
        setField(answer, "id", answerId);
        setField(answer, "user", DataUtil.user1());

        // Mock 설정
        given(answerRepository.findBoardById(boardId)).willReturn(board);
        given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));

        // when
        ApiResponse<AnswerResponse.Response> response = answerService.update(authUser, boardId, answerId, request);

        // then
        assertEquals("댓글 수정에 성공하였습니다.", response.getMessage());
    }

    @Test
    void 댓글_전체_조회_성공() {
        // given
        long boardId = DataUtil.one();
        int page = 1;
        int size = 10;
        Board board = DataUtil.bulletin1();
        Answer answer1 = DataUtil.answer1();
        Answer answer2 = new Answer("두 번째 답변 내용", DataUtil.user1(), board);

        setField(board, "id", boardId);
        setField(board, "boardType", BoardType.BOARD_NOTICE);
        setField(answer1, "id", DataUtil.one());
        setField(answer2, "id", DataUtil.two());

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Answer> answers = new PageImpl<>(List.of(answer1, answer2), pageable, 2);

        given(answerRepository.findBoardById(boardId)).willReturn(board);
        given(answerRepository.findByBoardId(boardId, pageable)).willReturn(answers);

        // when
        ApiResponse<Page<AnswerResponse.Response>> response = answerService.viewAll(boardId, page, size);

        // then
        assertEquals("댓글 조회(전체)에 성공하였습니다.", response.getMessage());
    }

    @Test
    void 댓글_단건_조회_성공() {
        // given
        long boardId = DataUtil.one();
        long answerId = DataUtil.two();
        Board board = DataUtil.bulletin1();
        Answer answer = DataUtil.answer1();

        setField(board, "id", boardId);
        setField(board, "boardType", BoardType.BOARD_NOTICE);
        setField(answer, "id", answerId);

        given(answerRepository.findBoardById(boardId)).willReturn(board);
        given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));

        // when
        ApiResponse<AnswerResponse.Response> response = answerService.viewOne(boardId, answerId);

        // then
        assertEquals("댓글 조회(단건)에 성공하였습니다.", response.getMessage());
    }

    @Test
    void 댓글_삭제_성공() {
        // given
        AuthUser authUser = DataUtil.authUser1();
        long boardId = DataUtil.one();
        long answerId = DataUtil.two();
        Board board = DataUtil.bulletin1();
        Answer answer = new Answer("삭제할 댓글 내용", DataUtil.user1(), board);

        setField(board, "id", boardId);
        setField(board, "boardType", BoardType.BOARD_NOTICE);
        setField(answer, "id", answerId);
        setField(answer, "user", DataUtil.user1());

        given(answerRepository.findBoardById(boardId)).willReturn(board);
        given(answerRepository.findById(answerId)).willReturn(Optional.of(answer));

        // when
        ApiResponse<Void> response = answerService.delete(authUser, boardId, answerId);

        // then
        assertEquals("댓글 삭제에 성공하였습니다.", response.getMessage());
    }

}

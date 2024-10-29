package com.sparta.doguin.domain.answer.service;

import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.answer.dto.AnswerRequest;
import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.entity.Answer;
import com.sparta.doguin.domain.answer.enums.AnswerType;
import com.sparta.doguin.domain.answer.repository.AnswerRepository;
import com.sparta.doguin.domain.board.BoardType;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.board.repository.BoardRepository;
import com.sparta.doguin.domain.common.exception.AnswerException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseAnswerEnum;
import com.sparta.doguin.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventAnswerService implements AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerType answerType = AnswerType.BOARD;

    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public ApiResponse<AnswerResponse.Response> create(AuthUser authUser, long boardId, AnswerRequest.Request request) {
        // 로그인한 사용자의 인증 정보
        User user = User.fromAuthUser(authUser);

        // 게시글이 존재하는지 확인
        Board board = answerRepository.findBoardById(boardId);
        if (board == null) {
            throw new AnswerException(ApiResponseAnswerEnum.BOARD_NOT_FOUND);
        }

        // 댓글 보드 타입 검증
        if(!board.getBoardType().equals(BoardType.BOARD_EVENT)) {
            throw new AnswerException(ApiResponseAnswerEnum.INVALID_BOARD_TYPE);
        }

        // 생성
        Answer answer = new Answer(request.content(), user, board);

        answerRepository.save(answer);

        return ApiResponse.of(ApiResponseAnswerEnum.APPLY_ANSWER_CREATE_SUCCESS, new AnswerResponse.Response(answer.getId(), answer.getContent()));
    }

    @Override
    @Transactional
    public ApiResponse<AnswerResponse.Response> update(AuthUser authUser, long boardId, long answerId, AnswerRequest.Request request) {
        // 로그인한 사용자의 인증 정보
        User user = User.fromAuthUser(authUser);

        // 게시글이 존재하는지 확인
        Board board = answerRepository.findBoardById(boardId);
        if (board == null) {
            throw new AnswerException(ApiResponseAnswerEnum.BOARD_NOT_FOUND);
        }

        // 댓글 보드 타입 검증
        if(!board.getBoardType().equals(BoardType.BOARD_EVENT)) {
            throw new AnswerException(ApiResponseAnswerEnum.INVALID_BOARD_TYPE);
        }

        // 댓글 찾기
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.COMMENT_ANSWER_NOT_FOUND));

        // 본인이 생성한 답변인지 확인
        if (!answer.getUser().getId().equals(user.getId())) {
            throw new AnswerException(ApiResponseAnswerEnum.UPDATE_ACCESS_DENIED);
        }

        // 수정
        answer.update(request);

        // 저장
        answerRepository.save(answer);

        // 성공 응답 반환
        return ApiResponse.of(ApiResponseAnswerEnum.COMMENT_ANSWER_UPDATE_SUCCESS, new AnswerResponse.Response(answer.getId(), answer.getContent()));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Page<AnswerResponse.Response>> viewAll(long boardId, int page, int size) {
        Pageable pageable = PageRequest.of(page -1, size);

        // 게시글이 존재하는지 확인
        Board board = answerRepository.findBoardById(boardId);
        if (board == null) {
            throw new AnswerException(ApiResponseAnswerEnum.BOARD_NOT_FOUND);
        }

        // 댓글 보드 타입 검증
        if(!board.getBoardType().equals(BoardType.BOARD_EVENT)) {
            throw new AnswerException(ApiResponseAnswerEnum.INVALID_BOARD_TYPE);
        }

        Page<AnswerResponse.Response> response = findByBoardId(boardId, pageable);

        return ApiResponse.of(ApiResponseAnswerEnum.COMMENT_ANSWER_FIND_ALL_SUCCESS, response);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<AnswerResponse.Response> viewOne(long boardId, long answerId) {
        // 게시글이 존재하는지 확인
        Board board = answerRepository.findBoardById(boardId);
        if (board == null) {
            throw new AnswerException(ApiResponseAnswerEnum.BOARD_NOT_FOUND);
        }

        // 댓글 보드 타입 검증
        if(!board.getBoardType().equals(BoardType.BOARD_EVENT)) {
            throw new AnswerException(ApiResponseAnswerEnum.INVALID_BOARD_TYPE);
        }

        // 댓글 찾기
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.COMMENT_ANSWER_NOT_FOUND));

        return ApiResponse.of(ApiResponseAnswerEnum.COMMENT_ANSWER_FIND_ONE_SUCCESS, new AnswerResponse.Response(answer.getId(), answer.getContent()));
    }

    @Override
    @Transactional
    public ApiResponse<Void> delete(AuthUser authUser, long boardId, long answerId) {
        // 로그인한 사용자의 인증 정보
        User user = User.fromAuthUser(authUser);

        // 게시글이 존재하는지 확인
        Board board = answerRepository.findBoardById(boardId);
        if (board == null) {
            throw new AnswerException(ApiResponseAnswerEnum.BOARD_NOT_FOUND);
        }

        // 댓글 보드 타입 검증
        if(!board.getBoardType().equals(BoardType.BOARD_EVENT)) {
            throw new AnswerException(ApiResponseAnswerEnum.INVALID_BOARD_TYPE);
        }

        // 댓글 찾기
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerException(ApiResponseAnswerEnum.COMMENT_ANSWER_NOT_FOUND));

        // 본인이 생성한 답변인지 확인
        if (!answer.getUser().getId().equals(user.getId())) {
            throw new AnswerException(ApiResponseAnswerEnum.UPDATE_ACCESS_DENIED);
        }

        // 삭제
        answerRepository.delete(answer);

        // 성공 응답 반환
        return ApiResponse.of(ApiResponseAnswerEnum.COMMENT_ANSWER_DELETE_SUCCESS);
    }



















    public Page<AnswerResponse.Response> findByBoardId(long boardId, Pageable pageable) {
        Page<Answer> answers = answerRepository.findByBoardId(boardId, pageable);
        Page<AnswerResponse.Response> response = answers
                .map(answer -> new AnswerResponse.Response(answer.getId(), answer.getContent()));

        return response;
    }
}

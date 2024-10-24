package com.sparta.doguin.domain.answer.service;

import com.sparta.doguin.domain.answer.AnswerType;
import com.sparta.doguin.domain.answer.dto.AnswerRequest;
import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.entity.Answer;
import com.sparta.doguin.domain.answer.repository.AnswerRepository;
import com.sparta.doguin.domain.common.exception.HandleNotFound;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseAnswerEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BulletinAnswerService implements AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerType answerType = AnswerType.BULLETIN_ANSWER;

    /**
     * 자유게시판 댓글 등록
     *
     * @param request 자유게시판 생성 시 필요한 정보가 담긴 객체
     * @since 1.0
     * @return 생성된 댓글의 정보를 포함하는 ApiResponse
     * @author 유태이
     */
    @Override
    @Transactional
    public ApiResponse<AnswerResponse.Response> create(AnswerRequest.Request request) {
        Answer newAnswer = new Answer(request.content(), answerType);
        answerRepository.save(newAnswer);
        return ApiResponse.of(ApiResponseAnswerEnum.COMMENT_ANSWER_CREATE_SUCCESS, new AnswerResponse.Response(newAnswer.getId(), newAnswer.getContent()));
    }

    /**
     * 자유게시판 댓글 수정
     *
     * @param answerId 수정할 댓글의 ID
     * @param request 수정할 댓글의 정보가 담긴 객체
     * @since 1.0
     * @throws HandleNotFound 댓글 수정 시 데이터가 없을 경우 발생
     * @return 수정된 댓글의 정보를 포함하는 ApiResponse
     * @author 유태이
     */
    @Override
    @Transactional
    public ApiResponse<AnswerResponse.Response> update(long answerId, AnswerRequest.Request request) {
        Answer answer = findById(answerId);
        answer.update(request);
        answerRepository.save(answer);
        return ApiResponse.of(ApiResponseAnswerEnum.COMMENT_ANSWER_UPDATE_SUCCESS, new AnswerResponse.Response(answer.getId(), answer.getContent()));
    }

    /**
     * 자유게시판 댓글 전체 조회
     *
     * @param page 조회할 페이지 번호(기본 값: 1)
     * @param size 한 페이지에 포함될 댓글 수(기본 값: 10)
     * @since 1.0
     * @return 요청한 페이지에 해당하는 댓글 목록이 포함 된 ApiResponse
     * @author 유태이
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Page<AnswerResponse.Response>> viewAll(long boardId, int page, int size) {
        Pageable pageable = PageRequest.of(page -1, size);
        Page<AnswerResponse.Response> response = findByBoardId(boardId, pageable);
        return ApiResponse.of(ApiResponseAnswerEnum.COMMENT_ANSWER_FIND_ALL_SUCCESS, response);
    }

    /**
     * 자유게시판 댓글 단건 조회
     *
     * @param answerId 조회할 댓글 ID
     * @since 1.0
     * @throws HandleNotFound 댓글 수정 시 데이터가 없을 경우 발생
     * @return 요청한 댓글의 정보가 포함 된 ApiResponse
     * @author 유태이
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<AnswerResponse.Response> viewOne(long answerId) {
        Answer answer = findById(answerId);
        return ApiResponse.of(ApiResponseAnswerEnum.COMMENT_ANSWER_FIND_ONE_SUCCESS, new AnswerResponse.Response(answer.getId(), answer.getContent()));
    }

    /**
     * 자유게시판 댓글 삭제
     *
     * @param answerId 삭제할 댓글의 ID
     * @since 1.0
     * @throws HandleNotFound 댓글 수정 시 데이터가 없을 경우 발생
     * @return 삭제 결과를 포함하는 ApiResponse
     * @author 유태이
     */
    @Override
    @Transactional
    public ApiResponse<Void> delete(long answerId) {
        Answer answer = findById(answerId);
        answerRepository.delete(answer);
        return ApiResponse.of(ApiResponseAnswerEnum.COMMENT_ANSWER_DELETE_SUCCESS);
    }

    /**
     * 자유게시판 댓글 ID로 조회
     *
     * @param answerId 조회할 댓글 ID
     * @throws HandleNotFound 댓글이 존재하지 않을 경우 발생
     * @return 해당 댓글 객체
     * @author 유태이
     */
    private Answer findById(long answerId) {
        return answerRepository.findById(answerId).orElseThrow(() -> new HandleNotFound(ApiResponseAnswerEnum.COMMENT_ANSWER_NOT_FOUND));
    }

    /**
     * 특정 게시글에 대한 댓글 목록을 페이지네이션하여 반환
     *
     * @param boardId 댓글 조회할 게시글 ID
     * @param pageable 페이지네이션 정보가 담긴 객체
     * @since 1.0
     * @return 게시글에 해당하는 댓글 목록을 변환한 Page 객체 반환
     * @author 유태이
     */
    public Page<AnswerResponse.Response> findByBoardId(long boardId, Pageable pageable) {
        Page<Answer> answers = answerRepository.findByBoardId(boardId, pageable);
        Page<AnswerResponse.Response> response = answers
                .map(answer -> new AnswerResponse.Response(answer.getId(), answer.getContent()));

        return response;
    }


}

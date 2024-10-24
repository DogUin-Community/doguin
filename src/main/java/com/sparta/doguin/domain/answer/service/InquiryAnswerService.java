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
public class InquiryAnswerService implements AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerType answerType = AnswerType.INQUIRY_ANSWER;

    /**
     * 1:1 문의 답변 등록
     *
     * @param request 1:1 문의 답변 생성 시 필요한 정보가 담긴 객체
     * @since 1.0
     * @return 생성 된 답변의 정보를 포함하는 ApiResponse
     * @author 유태이
     */
    @Override
    @Transactional
    public ApiResponse<AnswerResponse.Response> create(AnswerRequest.Request request) {
        Answer newAnswer = new Answer(request.content(), answerType);
        answerRepository.save(newAnswer);
        return ApiResponse.of(ApiResponseAnswerEnum.INQUIRY_ANSWER_CREATE_SUCCESS, new AnswerResponse.Response(newAnswer.getId(), newAnswer.getContent()));
    }

    /**
     * 1:1 문의 답변 수정
     *
     * @param answerId 수정할 답변 ID
     * @param request 수정할 답변의 정보가 담긴 객체
     * @since 1.0
     * @throws HandleNotFound 답변 수정 시 데이터가 없을 경우 발생
     * @return 수정 된 답변의 정보를 포함하는 ApiResponse
     * @author 유태이
     */
    @Override
    @Transactional
    public ApiResponse<AnswerResponse.Response> update(long answerId, AnswerRequest.Request request) {
        Answer answer = findById(answerId);
        answer.update(request);
        answerRepository.save(answer);
        return ApiResponse.of(ApiResponseAnswerEnum.INQUIRY_ANSWER_UPDATE_SUCCESS, new AnswerResponse.Response(answer.getId(), answer.getContent()));
    }

    /**
     * 1:1 문의 답변 전체 조회
     *
     * @param page 조회할 페이지 번호(기본 값: 1)
     * @param size 한 페이지에 포함될 질문 수(기본 값 10)
     * @since 1.0
     * @return 요청한 페이지에 해당하는 답변 목록이 포함 된 ApiResponse
     * @author 유태이
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Page<AnswerResponse.Response>> viewAll(int page, int size) {
        Pageable pageable = PageRequest.of(page -1, size);

        Page<Answer> answerList = answerRepository.findAllByAnswerType(pageable, answerType);

        Page<AnswerResponse.Response> response = answerList
                .map(answer -> new AnswerResponse.Response(answer.getId(), answer.getContent()));

        return ApiResponse.of(ApiResponseAnswerEnum.INQUIRY_ANSWER_FIND_ALL_SUCCESS, response);
    }

    /**
     * 1:1 문의 답변 단건 조회
     *
     * @param answerId 조회할 답변 ID
     * @since 1.0
     * @throws HandleNotFound 답변 수정 시 데이터가 없을 경우 발생
     * @return 요청한 답변 정보가 포함 된 ApiResponse
     * @author 유태이
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<AnswerResponse.Response> viewOne(long answerId) {
        Answer answer = findById(answerId);
        return ApiResponse.of(ApiResponseAnswerEnum.INQUIRY_ANSWER_FIND_ONE_SUCCESS, new AnswerResponse.Response(answer.getId(), answer.getContent()));
    }

    /**
     * 1:1 문의 답변 삭제
     *
     * @param answerId 삭제할 답변 ID
     * @since 1.0
     * @throws HandleNotFound 답변 수정 시 데이터가 없을 경우 발생
     * @return 삭제가 성공적으로 완료되었음을 나타내는 ApiResponse
     * @author 유태이
     */
    @Override
    @Transactional
    public ApiResponse<Void> delete(long answerId) {
        Answer answer = findById(answerId);
        answerRepository.delete(answer);
        return ApiResponse.of(ApiResponseAnswerEnum.INQUIRY_ANSWER_DELETE_SUCCESS);
    }

    /**
     * 1:1 문의 답변 ID로 조회
     *
     * @param answerId 조회할 답변 ID
     * @throws HandleNotFound 답변이 존재하지 않을 경우 발생
     * @return 해당 질문 객체
     * @author 유태이
     */
    private Answer findById(long answerId) {
        return answerRepository.findById(answerId).orElseThrow(() -> new HandleNotFound(ApiResponseAnswerEnum.INQUIRY_ANSWER_NOT_FOUND));
    }
}

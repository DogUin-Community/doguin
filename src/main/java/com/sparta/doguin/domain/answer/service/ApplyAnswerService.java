package com.sparta.doguin.domain.answer.service;

import com.sparta.doguin.domain.answer.AnswerType;
import com.sparta.doguin.domain.answer.dto.AnswerRequest;
import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.entity.Answer;
import com.sparta.doguin.domain.answer.repository.AnswerRepository;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseTest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("applyAnswerService")
@RequiredArgsConstructor
public class ApplyAnswerService implements AnswerService{

    private final AnswerRepository answerRepository;
    private final AnswerType answerType = AnswerType.APPLY_ANSWER;

    @Override
    @Transactional
    public ApiResponse<AnswerResponse.Response> create(AnswerRequest.Request request) {
        Answer newAnswer = new Answer(request.content(), answerType);
        answerRepository.save(newAnswer);
        return ApiResponse.of(ApiResponseTest.TEST_SUCCESS, new AnswerResponse.Response(newAnswer.getId(), newAnswer.getContent()));
    }

    @Override
    @Transactional
    public ApiResponse<AnswerResponse.Response> update(long answerId, AnswerRequest.Request request) {
        Answer answer = findById(answerId);
        answer.update(request);
        answerRepository.save(answer);
        return ApiResponse.of(ApiResponseTest.TEST_SUCCESS, new AnswerResponse.Response(answer.getId(), answer.getContent()));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Page<AnswerResponse.Response>> viewAll(int page, int size) {
        Pageable pageable = PageRequest.of(page -1, size);

        Page<Answer> answerList = answerRepository.findAllByAnswerType(pageable, answerType);

        Page<AnswerResponse.Response> response = answerList
                .map(answer -> new AnswerResponse.Response(answer.getId(), answer.getContent()));

        return ApiResponse.of(ApiResponseTest.TEST_SUCCESS, response);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<AnswerResponse.Response> viewOne(long answerId) {
        Answer answer = findById(answerId);
        return ApiResponse.of(ApiResponseTest.TEST_SUCCESS, new AnswerResponse.Response(answer.getId(), answer.getContent()));
    }

    @Override
    @Transactional
    public ApiResponse<Void> delete(long answerId) {
        Answer answer = findById(answerId);
        answerRepository.delete(answer);
        return ApiResponse.of(ApiResponseTest.TEST_SUCCESS);
    }

    private Answer findById(long answerId) {
        return answerRepository.findById(answerId).orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));
    }
}

package com.sparta.doguin.domain.answer.service;

import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.answer.dto.AnswerRequest;
import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.common.response.ApiResponse;
import org.springframework.data.domain.Page;

public interface AnswerService {

    // 일반 댓글
    default ApiResponse<AnswerResponse.Response> create(AuthUser authUser, long boardId, AnswerRequest.Request request) {
        throw new UnsupportedOperationException("create operation not supported");
    }
    default ApiResponse<AnswerResponse.Response> update(AuthUser authUser, long boardId, long answerId, AnswerRequest.Request request) {
        throw new UnsupportedOperationException("update operation not supported");
    }
    default ApiResponse<Page<AnswerResponse.Response>> viewAll(long boardId, int page, int size) {
        throw new UnsupportedOperationException("viewAll operation not supported");
    }
    default ApiResponse<AnswerResponse.Response> viewOne(long boardId, long answerId) {
        throw new UnsupportedOperationException("viewOne operation not supported");
    }
    default ApiResponse<Void> delete(AuthUser authUser, long boardId, long answerId) {
        throw new UnsupportedOperationException("delete operation not supported");
    }



    // 답변
    default ApiResponse<AnswerResponse.Response> createQuestionAnswer(AuthUser authUser, long questionId, AnswerRequest.Request request) {
        throw new UnsupportedOperationException("create operation not supported");
    }
    default ApiResponse<AnswerResponse.Response> updateQuestionAnswer(AuthUser authUser, long questionId, long answerId, AnswerRequest.Request request) {
        throw new UnsupportedOperationException("update operation not supported");
    }
    default ApiResponse<Page<AnswerResponse.GetResponse>> viewAllQuestionAnswer(long questionId, int page, int size) {
        throw new UnsupportedOperationException("viewAll operation not supported");
    }
    default ApiResponse<AnswerResponse.GetResponse> viewOneQuestionAnswer(long questionId, long answerId) {
        throw new UnsupportedOperationException("viewOne operation not supported");
    }
    default ApiResponse<Void> deleteQuestionAnswer(AuthUser authUser, long questionId, long answerId) {
        throw new UnsupportedOperationException("delete operation not supported");
    }


    // 대답변
    default ApiResponse<AnswerResponse.Response> createApplyAnswer(AuthUser authUser, long questionId, long answerId, AnswerRequest.Request request) {
        throw new UnsupportedOperationException("create operation not supported");
    }
    default ApiResponse<AnswerResponse.Response> updateApplyAnswer(AuthUser authUser, long questionId, long parentId, long answerId, AnswerRequest.Request request) {
        throw new UnsupportedOperationException("update operation not supported");
    }
    default ApiResponse<Void> deleteApplyAnswer(AuthUser authUser, long questionId, long parentId, long answerId) {
        throw new UnsupportedOperationException("delete operation not supported");
    }


}

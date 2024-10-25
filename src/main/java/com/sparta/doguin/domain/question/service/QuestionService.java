package com.sparta.doguin.domain.question.service;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.answer.entity.Answer;
import com.sparta.doguin.domain.answer.repository.AnswerRepository;
import com.sparta.doguin.domain.answer.service.QuestionAnswerService;
import com.sparta.doguin.domain.common.exception.HandleNotFound;
import com.sparta.doguin.domain.common.exception.QuestionException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseQuestionEnum;
import com.sparta.doguin.domain.question.dto.QuestionRequest;
import com.sparta.doguin.domain.question.dto.QuestionResponse;
import com.sparta.doguin.domain.question.entity.Question;
import com.sparta.doguin.domain.question.repository.QuestionRepository;
import com.sparta.doguin.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionAnswerService questionAnswerService;

    /**
     * 질문 생성
     *
     * @param request 질문 생성 시 필요한 정보가 담긴 객체
     * @since 1.0
     * @return 생성된 질문의 정보를 포함하는 ApiResponse 객체
     * @author 유태이
     */
    public ApiResponse<QuestionResponse.CreatedQuestion> createdQuestion(AuthUser authUser, QuestionRequest.CreatedQuestion request) {

        // 사용자가 로그인 했는지 검증
        if (authUser == null) {
            throw new QuestionException(ApiResponseQuestionEnum.QUESTION_UNAUTHORIZED_USER);
        }

        // 로그인한 사용자의 인증 정보
        User user = User.fromAuthUser(authUser);

        // 질문 생성
        Question newQuestion = new Question(request.title(),
                                            request.content(),
                                            request.firstCategory(),
                                            request.secondCategory(),
                                            request.lastCategory(),
                                            user);
        
        // 질문 저장
        questionRepository.save(newQuestion);

        // 성공 응답 반환
        return ApiResponse.of(ApiResponseQuestionEnum.QUESTION_CREATE_SUCCESS, new QuestionResponse.CreatedQuestion(newQuestion.getId(), newQuestion.getTitle(), newQuestion.getContent(), newQuestion.getFirstCategory(), newQuestion.getSecondCategory(), newQuestion.getLastCategory(), newQuestion.getQuestionStatus()));
    }

    /**
     * 질문 수정
     *
     * @param questionId 수정할 질문의 ID
     * @param request 수정할 질문의 정보가 담긴 객체
     * @since 1.0
     * @throws HandleNotFound 질문 수정 시 데이터가 없을 경우 발생
     * @return 수정 된 질문의 정보를 포함하는 ApiResponse 객체
     * @author 유태이
     */
    public ApiResponse<QuestionResponse.CreatedQuestion> updatedQuestion(AuthUser authUser, long questionId, QuestionRequest.UpdateQuestion request) {

        // 사용자가 로그인 했는지 검증
        if (authUser == null) {
            throw new QuestionException(ApiResponseQuestionEnum.QUESTION_UNAUTHORIZED_USER);
        }

        // 해당 댓글 있는지 검증
        Question question = findById(questionId);

        // 로그인한 사용자의 인증 정보
        User user = User.fromAuthUser(authUser);

        // 본인이 등록한 게시글인지 확인
        if (!question.getUser().getId().equals(user.getId())) {
            throw new QuestionException(ApiResponseQuestionEnum.QUESTION_UPDATE_ACCESS_DENIED);
        }

        // 질문 수정
        question.update(request);

        // 질문 저장
        questionRepository.save(question);

        // 성공 응답 반환
        return ApiResponse.of(ApiResponseQuestionEnum.QUESTION_UPDATE_SUCCESS, new QuestionResponse.CreatedQuestion(question.getId(), question.getTitle(), question.getContent(), question.getFirstCategory(), question.getSecondCategory(), question.getLastCategory(), question.getQuestionStatus()));
    }

    /**
     * 질문 다건 조회
     *
     * @param page 조회할 페이지 번호(기본 값: 1)
     * @param size 한 페이지에 포함될 질문 수(기본값: 10)
     * @since 1.0
     * @return 요청한 페이지에 해당하는 질문 목록
     * @author 유태이
     */
    public ApiResponse<Page<QuestionResponse.GetQuestions>> getQuestions(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Question> questions = questionRepository.findAll(pageable);

        Page<QuestionResponse.GetQuestions> response = questions
                .map(question -> new QuestionResponse.GetQuestions(question.getId(),
                                                                    question.getTitle(),
                                                                    question.getContent(),
                                                                    question.getFirstCategory(),
                                                                    question.getSecondCategory(),
                                                                    question.getLastCategory(),
                                                                    question.getQuestionStatus()));

        return ApiResponse.of(ApiResponseQuestionEnum.QUESTION_FIND_ALL_SUCCESS, response);
    }

    /**
     * 질문 단건 조회
     *
     * @param questionId 조회할 질문의 ID
     * @since 1.0
     * @throws HandleNotFound 질문 수정 시 데이터가 없을 경우 발생
     * @return 요청한 질문
     * @author 유태이
     */
    public ApiResponse<QuestionResponse.GetQuestion> getQuestion(long questionId) {
        // 질문 가져오기
        Question question = findById(questionId);

        Pageable pageable = PageRequest.of(0, 10); // 필요에 따라 페이지 크기를 조정 가능
        Page<Answer> answers = answerRepository.findByQuestionId(questionId, pageable);

        // 답변과 대답변 리스트 변환
        Page<AnswerResponse.GetResponse> comment = answers.map(answer -> {
            List<AnswerResponse.GetResponse> commentResponse = answerRepository.findByParentId(answer.getId()).stream()
                    .map(comments -> new AnswerResponse.GetResponse(comments.getId(), comments.getContent(), new ArrayList<>()))
                    .toList();

            return new AnswerResponse.GetResponse(answer.getId(), answer.getContent(), commentResponse);
        });

        // 질문과 답변 데이터 통합하여 반환
        QuestionResponse.GetQuestion response = new QuestionResponse.GetQuestion(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                question.getFirstCategory(),
                question.getSecondCategory(),
                question.getLastCategory(),
                question.getQuestionStatus(),
                comment
        );

        return ApiResponse.of(ApiResponseQuestionEnum.QUESTION_FIND_ONE_SUCCESS, response);
    }

    /**
     * 질문 삭제
     *
     * @param questionId 삭제할 질문의 ID
     * @since 1.0
     * @throws HandleNotFound 질문 삭제 시 데이터가 없을 경우 발생
     * @return 삭제 성공 ApiResponse 객체
     * @author 유태이
     */
    public ApiResponse<Void> deleteQuestion(AuthUser authUser, long questionId) {

        // 사용자가 로그인 했는지 검증
        if (authUser == null) {
            throw new QuestionException(ApiResponseQuestionEnum.QUESTION_UNAUTHORIZED_USER);
        }

        // 해당 댓글 있는지 검증
        Question question = findById(questionId);

        // 로그인한 사용자의 인증 정보
        User user = User.fromAuthUser(authUser);

        // 본인이 등록한 게시글인지 확인
        if (!question.getUser().getId().equals(user.getId())) {
            throw new QuestionException(ApiResponseQuestionEnum.QUESTION_UPDATE_ACCESS_DENIED);
        }

        // 질문 삭제
        questionRepository.delete(question);

        // 성공 응답 반환
        return ApiResponse.of(ApiResponseQuestionEnum.QUESTION_DELETE_SUCCESS);
    }












    /**
     * 질문 ID로 질문 조회
     *
     * @param questionId 조회할 질문 ID
     * @throws HandleNotFound 질문이 존재하지 않을 경우 발생
     * @return 해당 질문 객체
     */
    public Question findById(long questionId) {
        return questionRepository.findById(questionId).orElseThrow(() -> new HandleNotFound(ApiResponseQuestionEnum.QUESTION_NOT_FOUND));
    }

    /**
     * 주어진 사용자 ID에 해당하는 질문 목록을 반환
     *
     * @param authUser 로그인한 사용자 정보(사용자 ID)
     * @return 주어진 사용자 ID에 해당하는 질문의 페이징 결과
     */
    public List<Question> findAllByUserId(AuthUser authUser) {
        return questionRepository.findAllByUserId(authUser.getUserId());
    }












}

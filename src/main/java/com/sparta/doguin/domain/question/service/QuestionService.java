package com.sparta.doguin.domain.question.service;

import com.sparta.doguin.domain.answer.dto.AnswerResponse;
import com.sparta.doguin.domain.common.exception.HandleNotFound;
import com.sparta.doguin.domain.common.exception.QuestionException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseQuestionEnum;
import com.sparta.doguin.domain.question.dto.QuestionRequest;
import com.sparta.doguin.domain.question.dto.QuestionResponse;
import com.sparta.doguin.domain.question.entity.Question;
import com.sparta.doguin.domain.question.repository.QuestionRepository;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    private final QuestionViewService questionViewService;
    private final static String QUESTION_CACHE = "questionBoard";
    private final static String QUESTION_POPULAR = "questionPopular";
    private final RedisTemplate redisTemplate;

    /**
     * 질문 생성
     *
     * @param request 질문 생성 시 필요한 정보가 담긴 객체
     * @since 1.0
     * @return 생성된 질문의 정보를 포함하는 ApiResponse 객체
     * @author 유태이
     */
    public ApiResponse<QuestionResponse.CreatedQuestion> createdQuestion(AuthUser authUser, QuestionRequest.CreatedQuestion request) {
        // 로그인한 사용자의 인증 정보
        User user = User.fromAuthUser(authUser);
        // 질문 생성
        Question newQuestion = new Question(request.title(), request.content(), request.firstCategory(), request.secondCategory(), request.lastCategory(), user);
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
    @Cacheable(value = QUESTION_CACHE, key = "'단건조회' + #questionId")
    public QuestionResponse.GetQuestion getQuestion(User user, long questionId) {

        // 질문과 답변 및 대답변 조회
        Question question = questionRepository.findByIdWithAnswers(questionId)
                .orElseThrow(() -> new HandleNotFound(ApiResponseQuestionEnum.QUESTION_NOT_FOUND));

        if (user!=null){
            questionViewService.trackUserView(questionId, user.getId());
        }

        Long viewCount = questionViewService.getHourUniqueViewCount(questionId) + (question.getView() != null ? question.getView() : 0L);

        // 답변과 대답변 리스트 반환
        List<AnswerResponse.GetResponse> answers = question.getAnswerList().stream()
                .map(answer -> {
                    // answer의 parent가 동일한 대답변 필터링
                    List<AnswerResponse.GetResponse> commentResponses = question.getAnswerList().stream()
                            .filter(apply -> apply.getParent() != null && apply.getParent().getId().equals(answer.getId()))
                            .map(apply -> new AnswerResponse.GetResponse(apply.getId(), apply.getContent(), new ArrayList<>()))
                            .toList();

                    return new AnswerResponse.GetResponse(answer.getId(), answer.getContent(), commentResponses);
                })
                .toList();

        return new QuestionResponse.GetQuestion(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                question.getFirstCategory(),
                question.getSecondCategory(),
                question.getLastCategory(),
                question.getQuestionStatus(),
                viewCount,
                answers
        );
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
     * 질문 검색: 제목과 내용을 기준으로 검색, 페이징된 결과를 반환
     * 검색 조건이 없을 경우 모든 질문을 페이징하여 반환
     *
     * @param pageable 페이징 정보
     * @param title 검색할 제목(선택)
     * @param content 검색할 내용(선택)
     * @return 제목 및 내용에 따라 필터링된 질문 목록을 담은 객체
     * @since 1.0
     * @author 유태이
     */
    @Transactional(readOnly = true)
    public ApiResponse<Page<QuestionResponse.SearchQuestion>> search(Pageable pageable, String title, String content) {
        Page<Question> search = questionRepository.search(title, content, pageable);
        Page<QuestionResponse.SearchQuestion> response = search.map(QuestionResponse.SearchQuestion::of);

        return ApiResponse.of(ApiResponseQuestionEnum.QUESTION_SEARCH_SUCCESS, response);
    }

    public Page<Long> viewPopular(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Set<Long> popularQuestionIds = questionViewService.viewPopularQuestionList();

        if (popularQuestionIds == null || popularQuestionIds.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        List<Long> popularQuestionIdList = popularQuestionIds.stream().collect(Collectors.toList());

        int start = Math.min((page-1) * size, popularQuestionIdList.size());
        int end = Math.min(start + size, popularQuestionIdList.size());
        List<Long> paginatedQuestionIds = popularQuestionIdList.subList(start, end);

        return new PageImpl<>(paginatedQuestionIds,pageable, popularQuestionIdList.size());

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
     * @return 주어진 사용자 ID에 해당하는 질문의 리스트 결과
     */
    public List<Question> findAllByUserId(AuthUser authUser) {
        return questionRepository.findAllByUserId(authUser.getUserId());
    }
}

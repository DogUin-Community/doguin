package com.sparta.doguin.domain.discussions.service;

import com.sparta.doguin.domain.common.exception.DiscussionException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseDiscussionEnum;
import com.sparta.doguin.domain.discussions.dto.ReplyRequest;
import com.sparta.doguin.domain.discussions.dto.ReplyResponse;
import com.sparta.doguin.domain.discussions.entity.Discussion;
import com.sparta.doguin.domain.discussions.entity.Reply;
import com.sparta.doguin.domain.discussions.repository.DiscussionRepository;
import com.sparta.doguin.domain.discussions.repository.ReplyRepository;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.service.UserService;
import com.sparta.doguin.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private static final String DISCUSSION_KEY_PREFIX = "discussion:";

    private final DiscussionRepository discussionRepository;
    private final ReplyRepository replyRepository;
    private final UserService userService;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 새로운 답변을 추가하는 메서드
     *
     * @param discussionId / 답변이 추가될 토론의 ID
     * @param request      / 답변 생성에 필요한 데이터 (내용)
     * @param authUser     / 답변 작성자 정보
     * @return ApiResponse<ReplyResponse.AddReplyResponse> / 생성된 답변 응답 데이터 반환
     * @since 1.0
     * @author 최욱연
     */
    public ApiResponse<ReplyResponse.AddReplyResponse> addReply(Long discussionId, ReplyRequest.CreateRequest request, AuthUser authUser) {
        Discussion discussion = discussionRepository.findByIdOrThrow(discussionId);

        // 토론이 활성 상태인지 확인 (Redis TTL 및 closed 필드 모두 확인)
        validateDiscussionIsActive(discussion);

        User user = userService.findById(authUser.getUserId());
        Reply reply = new Reply(request.content(), discussion, user);
        replyRepository.save(reply);

        return createAddReplyResponse(reply, user);
    }

    /**
     * 토론이 활성 상태인지 확인하는 메서드
     *
     * @param discussion / 활성 상태를 확인할 토론 객체
     * @throws DiscussionException / 토론이 비활성 상태일 경우 예외 발생
     * @since 1.0
     * @author 최욱연
     */
    private void validateDiscussionIsActive(Discussion discussion) {
        // Redis에 TTL이 만료된 경우 (키가 존재하지 않는 경우) 혹은 DB에서 closed 상태인 경우
        String discussionKey = DISCUSSION_KEY_PREFIX + discussion.getId();
        if (Boolean.FALSE.equals(redisTemplate.hasKey(discussionKey)) || discussion.isClosed()) {
            throw new DiscussionException(ApiResponseDiscussionEnum.DISCUSSION_ALREADY_CLOSED);
        }
    }

    /**
     * 답변을 삭제하는 메서드
     *
     * @param replyId  / 삭제할 답변의 ID
     * @param authUser / 답변 삭제 요청한 사용자 정보
     * @return ApiResponse<Void> / 답변 성공 응답 반환
     * @throws DiscussionException / 답변 소유자가 아닌 경우 예외 발생
     * @since 1.0
     * @author 최욱연
     */
    public ApiResponse<Void> deleteReply(Long replyId, AuthUser authUser) {
        Reply reply = findReplyById(replyId);
        validateOwnership(reply, authUser);

        replyRepository.delete(reply);
        return ApiResponse.of(ApiResponseDiscussionEnum.REPLY_DELETE_SUCCESS);
    }

    /**
     * 답변을 수정하는 메서드
     *
     * @param replyId  / 수정할 답변의 ID
     * @param request  / 수정할 답변 데이터 (내용)
     * @param authUser / 답변 수정 요청한 사용자 정보
     * @return ApiResponse<ReplyResponse.SingleResponse> / 수정된 답변 응답 데이터 반환
     * @throws DiscussionException / 답변소유자가 아닌 경우 예외 발생
     * @since 1.0
     * @author 최욱연
     */
    public ApiResponse<ReplyResponse.SingleResponse> updateReply(Long replyId, ReplyRequest.UpdateRequest request, AuthUser authUser) {
        Reply reply = findReplyById(replyId);
        validateOwnership(reply, authUser);

        reply.updateContent(request.content());
        replyRepository.save(reply);

        return createUpdateReplyResponse(reply);
    }

    /**
     * 답변의 작성자를 확인하는 메서드
     *
     * @param reply    / 확인할 답변 객체
     * @param authUser / 현재 사용자 정보
     * @throws DiscussionException / 작성자가 아닐 경우 예외 발생
     * @since 1.0
     * @author 최욱연
     */
    private void validateOwnership(Reply reply, AuthUser authUser) {
        if (!reply.getUser().getId().equals(authUser.getUserId())) {
            throw new DiscussionException(ApiResponseDiscussionEnum.NOT_REPLY_OWNER);
        }
    }

    /**
     * 답변을 ID로 조회하는 메서드
     *
     * @param replyId / 조회할 답변의 ID
     * @return Reply / 조회된 답변 객체
     * @throws DiscussionException / 답변이 존재하지 않을 경우 예외 발생
     * @since 1.0
     * @author 최욱연
     */
    private Reply findReplyById(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new DiscussionException(ApiResponseDiscussionEnum.REPLY_NOT_FOUND));
    }

    /**
     * 생성된 답변 응답을 구성하는 메서드
     *
     * @param reply / 생성된 답변 객체
     * @param user  / 답변 작성자 정보
     * @return ApiResponse<ReplyResponse.AddReplyResponse> / 답변 생성 성공 응답 반환
     * @since 1.0
     * @author 최욱연
     */
    private ApiResponse<ReplyResponse.AddReplyResponse> createAddReplyResponse(Reply reply, User user) {
        return ApiResponse.of(ApiResponseDiscussionEnum.REPLY_CREATE_SUCCESS,
                new ReplyResponse.AddReplyResponse(
                        reply.getId(),
                        reply.getContent(),
                        user.getNickname(),
                        reply.getCreatedAt().toString()
                ));
    }

    /**
     * 수정된 답변 응답을 구성하는 메서드
     *
     * @param reply / 수정된 답변 객체
     * @return ApiResponse<ReplyResponse.SingleResponse> / 답변 수정 성공 응답 반환
     * @since 1.0
     * @author 최욱연
     */
    private ApiResponse<ReplyResponse.SingleResponse> createUpdateReplyResponse(Reply reply) {
        return ApiResponse.of(ApiResponseDiscussionEnum.REPLY_UPDATE_SUCCESS,
                new ReplyResponse.SingleResponse(
                        reply.getId(),
                        reply.getContent(),
                        reply.getUser().getNickname(),
                        reply.getCreatedAt().toString(),
                        reply.getUpdatedAt().toString()
                ));
    }
}

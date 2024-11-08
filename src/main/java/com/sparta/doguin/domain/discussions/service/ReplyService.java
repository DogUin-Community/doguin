package com.sparta.doguin.domain.discussions.service;

import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;
import com.sparta.doguin.domain.attachment.entity.Attachment;
import com.sparta.doguin.domain.attachment.repository.AttachmentRepository;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentDeleteService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUploadService;
import com.sparta.doguin.domain.common.exception.DiscussionException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseDiscussionEnum;
import com.sparta.doguin.domain.discussions.dto.DiscussionAttachmentResponse;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private static final String DISCUSSION_KEY_PREFIX = "discussion:";

    private final DiscussionRepository discussionRepository;
    private final ReplyRepository replyRepository;
    private final UserService userService;
    private final AttachmentUploadService attachmentUploadService;
    private final AttachmentDeleteService attachmentDeleteService;
    private final AttachmentRepository attachmentRepository;
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
    public ApiResponse<ReplyResponse.AddReplyResponse> addReply(Long discussionId, List<MultipartFile> attachments, ReplyRequest.CreateRequest request, AuthUser authUser) {
        Discussion discussion = discussionRepository.findByIdOrThrow(discussionId);
        validateDiscussionIsActive(discussion);

        User user = userService.findById(authUser.getUserId());
        Reply reply = new Reply(request.content(), discussion, user);
        replyRepository.save(reply);

        if (attachments != null && !attachments.isEmpty()) {
            attachmentUploadService.upload(attachments, authUser, reply.getId(), AttachmentTargetType.REPLY);
        }

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
    public ApiResponse<ReplyResponse.SingleResponse> updateReply(Long replyId, List<MultipartFile> newAttachments, List<Long> attachmentIdsToDelete, ReplyRequest.UpdateRequest request, AuthUser authUser) {
        // 1. 답변 조회 및 소유권 검증
        Reply reply = findReplyById(replyId);
        validateOwnership(reply, authUser);

        // 2. 첨부 파일 처리 (삭제 및 업로드)
        handleAttachments(replyId, authUser, attachmentIdsToDelete, newAttachments);

        // 3. 답변 내용 업데이트
        reply.updateContent(request.content());
        replyRepository.save(reply);

        // 4. 수정된 답변에 대한 응답 생성 및 반환
        return createUpdateReplyResponse(reply);
    }

    private void handleAttachments(Long replyId, AuthUser authUser, List<Long> attachmentIdsToDelete, List<MultipartFile> newAttachments) {
        // 첨부 파일 삭제 처리
        if (attachmentIdsToDelete != null && !attachmentIdsToDelete.isEmpty()) {
            List<Long> validAttachmentIds = attachmentRepository.findAllAttachmentIdByUserIdAndTagertIdAndTarget(
                    authUser.getUserId(), replyId, AttachmentTargetType.REPLY);

            List<Long> idsToDelete = attachmentIdsToDelete.stream()
                    .filter(validAttachmentIds::contains)
                    .collect(Collectors.toList());

            if (!idsToDelete.isEmpty()) {
                attachmentDeleteService.delete(authUser, idsToDelete);
            }
        }

        // 새 첨부 파일 업로드 처리
        if (newAttachments != null && !newAttachments.isEmpty()) {
            attachmentUploadService.upload(newAttachments, authUser, replyId, AttachmentTargetType.REPLY);
        }
    }

    private List<DiscussionAttachmentResponse> getAttachmentResponses(Long targetId, AttachmentTargetType targetType) {
        // targetId와 targetType에 따라 첨부 파일 ID를 가져옴
        List<Long> attachmentIds = attachmentRepository.findAllAttachmentIdByTagertIdAndTarget(targetId, targetType);

        // 가져온 ID 리스트로 실제 첨부 파일 객체를 조회
        List<Attachment> attachments = attachmentRepository.findAllByAttachment(attachmentIds);

        // 첨부 파일 객체를 `DiscussionAttachmentResponse` 객체로 변환하여 반환
        return attachments.stream()
                .map(attachment -> new DiscussionAttachmentResponse(
                        attachment.getId(),
                        attachment.getAttachment_original_name(),
                        "/attachments/" + attachment.getAttachment_relative_path()
                ))
                .collect(Collectors.toList());
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
     */
    private ApiResponse<ReplyResponse.AddReplyResponse> createAddReplyResponse(Reply reply, User user) {
        List<DiscussionAttachmentResponse> attachmentResponses = getAttachmentResponses(reply.getId(), AttachmentTargetType.REPLY);

        return ApiResponse.of(ApiResponseDiscussionEnum.REPLY_CREATE_SUCCESS,
                new ReplyResponse.AddReplyResponse(
                        reply.getId(),
                        reply.getContent(),
                        user.getNickname(),
                        reply.getCreatedAt().toString(),
                        attachmentResponses
                ));
    }

    /**
     * 수정된 답변 응답을 구성하는 메서드
     *
     * @param reply / 수정된 답변 객체
     * @return ApiResponse<ReplyResponse.SingleResponse> / 답변 수정 성공 응답 반환
     * @since 1.0
     */
    private ApiResponse<ReplyResponse.SingleResponse> createUpdateReplyResponse(Reply reply) {
        // targetType을 추가로 전달
        List<DiscussionAttachmentResponse> attachmentResponses = getAttachmentResponses(reply.getId(), AttachmentTargetType.REPLY);

        return ApiResponse.of(ApiResponseDiscussionEnum.REPLY_UPDATE_SUCCESS,
                new ReplyResponse.SingleResponse(
                        reply.getId(),
                        reply.getContent(),
                        reply.getUser().getNickname(),
                        reply.getCreatedAt().toString(),
                        reply.getUpdatedAt().toString(),
                        attachmentResponses
                ));
    }
}

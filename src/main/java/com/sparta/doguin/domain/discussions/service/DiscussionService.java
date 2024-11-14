package com.sparta.doguin.domain.discussions.service;

import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;
import com.sparta.doguin.domain.attachment.entity.Attachment;
import com.sparta.doguin.domain.attachment.model.AttachmentResponse;
import com.sparta.doguin.domain.attachment.repository.AttachmentRepository;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentDeleteService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentGetService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUploadService;
import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import com.sparta.doguin.domain.bookmark.model.BookmarkRequest;
import com.sparta.doguin.domain.bookmark.service.BookmarkService;
import com.sparta.doguin.domain.common.exception.DiscussionException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseDiscussionEnum;
import com.sparta.doguin.domain.discussions.dto.DiscussionAttachmentResponse;
import com.sparta.doguin.domain.discussions.dto.DiscussionRequest;
import com.sparta.doguin.domain.discussions.dto.DiscussionResponse;
import com.sparta.doguin.domain.discussions.dto.ReplyResponse;
import com.sparta.doguin.domain.discussions.entity.Discussion;
import com.sparta.doguin.domain.discussions.repository.DiscussionRepository;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.service.UserService;
import com.sparta.doguin.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DiscussionService {

    private final DiscussionRepository discussionRepository;
    private final UserService userService;
    private final AttachmentUploadService attachmentUploadService;
    private final AttachmentDeleteService attachmentDeleteService;
    private final BookmarkService bookmarkService;
    private final AttachmentRepository attachmentRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final Duration DISCUSSION_TTL = Duration.ofDays(90);

    /**
     * 새로운 토론을 생성하는 메서드
     *
     * @param authUser / 토론을 생성하는 사용자 정보
     * @param request  / 토론 생성에 필요한 데이터 (제목, 내용)
     * @return ApiResponse<DiscussionResponse.SingleResponse> / 생성된 토론 응답 데이터 반환
     * @author 최욱연
     * @since 1.0
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ApiResponse<DiscussionResponse.SingleResponse> createDiscussion(AuthUser authUser, List<MultipartFile> attachments, DiscussionRequest.CreateRequest request) {
        if (attachments == null) {
            attachments = List.of();
        }

        User user = userService.findById(authUser.getUserId());
        Discussion discussion = new Discussion(request.title(), request.content(), user);
        discussionRepository.save(discussion);

        if (!attachments.isEmpty()) {
            attachmentUploadService.upload(attachments, authUser, discussion.getId(), AttachmentTargetType.DISCUSSION);
        }

        // 최신 첨부파일을 가져오기 위해 다시 데이터를 조회
        discussion = discussionRepository.findByIdOrThrow(discussion.getId());

        setDiscussionTTLInCache(discussion.getId());
        return ApiResponse.of(ApiResponseDiscussionEnum.DISCUSSION_CREATE_SUCCESS, toSingleResponse(discussion));
    }

    /**
     * 특정 토론을 조회하는 메서드
     *
     * @param discussionId / 조회할 토론의 ID
     * @param authUser     / 토론 조회를 요청한 사용자 정보
     * @return DiscussionResponse.SingleResponse / 조회된 토론 데이터 반환
     * @throws DiscussionException / 토론이 존재하지 않을 때 발생되는 예외
     * @author 최욱연
     * @since 1.0
     */
    @Cacheable(value = "discussionsCache", key = "'discussion_' + #discussionId")
    public DiscussionResponse.SingleResponse getDiscussion(Long discussionId, AuthUser authUser) {
        Discussion discussion = discussionRepository.findByIdOrThrow(discussionId);

        if (discussion == null) {
            throw new DiscussionException(ApiResponseDiscussionEnum.DISCUSSION_NOT_FOUND);
        }

        discussion.checkAndCloseDiscussion();

        // 사용자별 중복 조회 방지 처리
        String userViewKey = "viewed:discussion:" + discussionId + ":user:" + authUser.getUserId();
        if (Boolean.FALSE.equals(redisTemplate.hasKey(userViewKey))) {
            discussion.incrementViewCount(authUser.getUserId());
            discussionRepository.save(discussion);
            redisTemplate.opsForValue().set(userViewKey, "viewed", Duration.ofDays(1));
        }

        return toSingleResponse(discussion);
    }

    /**
     * 모든 토론을 페이지 단위로 조회하며, 진행 중인 토론이 우선적으로 반환되는 메서드
     *
     * @param pageable / 페이지 정보 (페이지 번호, 페이지 크기, 정렬 옵션 등)
     * @return Page<DiscussionResponse.ListResponse> / 조회된 토론 목록을 페이지 단위로 반환
     * @author 최욱연
     * @since 1.0
     */
    @Cacheable(value = "discussionsCache", key = "'allDiscussions_page_' + #pageable.pageNumber + '_size_' + #pageable.pageSize")
    public Page<DiscussionResponse.ListResponse> getAllDiscussions(Pageable pageable) {
        Page<Discussion> discussions = discussionRepository.findAllByOrderByClosedAscCreatedAtDesc(pageable);
        discussions.forEach(Discussion::checkAndCloseDiscussion);

        return discussions.map(this::toListResponse);
    }

    /**
     * 조건에 맞는 토론을 검색하는 메서드
     *
     * @param pageable / 페이지 정보 (페이지 번호, 페이지 크기, 정렬 옵션 등)
     * @param title    / 검색할 제목
     * @param content  / 검색할 본문
     * @param nickname / 검색할 사용자 닉네임
     * @return Page<DiscussionResponse.ListResponse> / 검색된 토론 목록을 페이지 단위로 반환
     * @author 최욱연
     * @since 1.0
     */
    @Transactional(readOnly = true)
    public Page<DiscussionResponse.ListResponse> searchDiscussions(Pageable pageable, String title, String content, String nickname) {
        Page<Discussion> discussions = discussionRepository.search(title, content, nickname, pageable);
        return discussions.map(this::toListResponse);
    }

    /**
     * 토론을 수정하는 메서드
     *
     * @param discussionId / 수정할 토론의 ID
     * @param request      / 수정할 토론 데이터 (제목, 내용)
     * @param authUser     / 토론 수정 요청한 사용자 정보
     * @return DiscussionResponse.SingleResponse / 수정된 토론 데이터 반환
     * @throws DiscussionException / 토론 소유자가 아닐 때 발생되는 예외
     * @author 최욱연
     * @since 1.0
     */
    @CacheEvict(value = "discussionsCache", key = "'allDiscussions'")
    @CachePut(value = "discussionsCache", key = "'discussion_' + #discussionId")
    public DiscussionResponse.SingleResponse updateDiscussion(
            Long discussionId,
            DiscussionRequest.UpdateRequest request,
            AuthUser authUser,
            List<Long> attachmentIdsToDelete,
            List<MultipartFile> newAttachments) {

        Discussion discussion = discussionRepository.findByIdOrThrow(discussionId);
        validateOwner(discussion, authUser);

        // 첨부파일 처리
        handleAttachments(discussionId, authUser, attachmentIdsToDelete, newAttachments);

        // 토론 내용 수정
        discussion.update(request.title(), request.content());
        discussionRepository.save(discussion);

        return toSingleResponse(discussion);
    }

    /**
     * 첨부파일을 처리하는 메서드
     *
     * @param discussionId          / 토론 ID
     * @param authUser              / 현재 사용자
     * @param attachmentIdsToDelete / 삭제할 첨부파일 ID 목록
     * @param newAttachments        / 새로 추가할 첨부파일 목록
     * @since 1.0
     * @author 최욱연
     */
    private void handleAttachments(
            Long discussionId,
            AuthUser authUser,
            List<Long> attachmentIdsToDelete,
            List<MultipartFile> newAttachments) {

        if (attachmentIdsToDelete != null && !attachmentIdsToDelete.isEmpty()) {
            List<Long> validAttachmentIds = attachmentRepository.findAllAttachmentIdByUserIdAndTagertIdAndTarget(
                    authUser.getUserId(), discussionId, AttachmentTargetType.DISCUSSION);

            List<Long> idsToDelete = attachmentIdsToDelete.stream()
                    .filter(validAttachmentIds::contains)
                    .collect(Collectors.toList());

            if (!idsToDelete.isEmpty()) {
                attachmentDeleteService.delete(authUser, idsToDelete);
            }
        }

        if (newAttachments != null && !newAttachments.isEmpty()) {
            attachmentUploadService.upload(newAttachments, authUser, discussionId, AttachmentTargetType.DISCUSSION);
        }
    }

    /**
     * 토론을 삭제하는 메서드
     *
     * @param discussionId / 삭제할 토론의 ID
     * @param authUser     / 토론 삭제 요청한 사용자 정보
     * @throws DiscussionException / 토론 소유자가 아닐 때 발생되는 예외
     * @author 최욱연
     * @since 1.0
     */
    @CacheEvict(value = "discussionsCache", key = "'discussion_' + #discussionId")
    public void deleteDiscussion(Long discussionId, AuthUser authUser) {
        Discussion discussion = discussionRepository.findByIdOrThrow(discussionId);
        validateOwner(discussion, authUser);

        discussionRepository.delete(discussion);
    }

    /**
     * Redis에서 토론 TTL 설정 메서드
     *
     * @param discussionId / TTL을 설정할 토론 ID
     * @author 최욱연
     * @since 1.0
     */
    private void setDiscussionTTLInCache(Long discussionId) {
        String discussionKey = "discussion:" + discussionId;
        // Redis에 3개월 TTL 설정
        redisTemplate.opsForValue().set(discussionKey, "active", DISCUSSION_TTL);
    }

    /**
     * 토론의 작성자를 확인하는 메서드
     *
     * @param discussion / 확인할 토론 객체
     * @param authUser   / 현재 사용자 정보
     * @throws DiscussionException / 작성자가 아닐 경우 예외 발생
     * @author 최욱연
     * @since 1.0
     */
    private void validateOwner(Discussion discussion, AuthUser authUser) {
        if (!discussion.getUser().getId().equals(authUser.getUserId())) {
            throw new DiscussionException(ApiResponseDiscussionEnum.NOT_DISCUSSION_OWNER);
        }
    }

    /**
     * 토론을 북마크하거나 북마크를 제거하는 메서드
     *
     * @param discussionId / 북마크할 토론의 ID
     * @param authUser     / 사용자 정보
     * @since 1.0
     * @author 최욱연
     */
    public void toggleBookmark(Long discussionId, AuthUser authUser) {
        BookmarkRequest.BookmarkRequestCreate reqDto = new BookmarkRequest.BookmarkRequestCreate(discussionId, BookmarkTargetType.DISCUSSION);
        bookmarkService.togleBookmark(reqDto, authUser);
    }

    /**
     * 특정 대상의 첨부 파일 응답 리스트를 가져오는 메서드
     *
     * @param targetId   / 첨부 파일을 가져올 대상의 ID
     * @param targetType / 대상 타입
     * @return List<DiscussionAttachmentResponse> / 첨부 파일 응답 리스트
     * @since 1.0
     * @author 최욱연
     */
    private List<DiscussionAttachmentResponse> getAttachmentResponses(Long targetId, AttachmentTargetType targetType) {
        List<Long> attachmentIds = attachmentRepository.findAllAttachmentIdByTagertIdAndTarget(targetId, targetType);
        List<Attachment> attachments = attachmentRepository.findAllByAttachment(attachmentIds);

        return attachments.stream()
                .map(attachment -> new DiscussionAttachmentResponse(
                        attachment.getId(),
                        attachment.getAttachment_original_name(),
                        "/attachments/" + attachment.getAttachment_relative_path()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 단일 토론 객체를 SingleResponse DTO로 변환하는 메서드
     *
     * @param discussion / 변환할 토론 객체
     * @return DiscussionResponse.SingleResponse / 변환된 응답 DTO
     * @since 1.0
     * @author 최욱연
     */
    private DiscussionResponse.SingleResponse toSingleResponse(Discussion discussion) {
        List<DiscussionAttachmentResponse> attachmentResponses = getAttachmentResponses(discussion.getId(), AttachmentTargetType.DISCUSSION);
        return new DiscussionResponse.SingleResponse(
                discussion.getId(),
                discussion.getTitle(),
                discussion.getContent(),
                discussion.getNickname(),
                discussion.getViewCount(),
                discussion.isClosed() ? "종료" : "진행중",
                discussion.getCreatedAt().toString(),
                discussion.getUpdatedAt().toString(),
                discussion.getReplies().stream()
                        .map(reply -> {
                            List<DiscussionAttachmentResponse> replyAttachmentResponses = getAttachmentResponses(reply.getId(), AttachmentTargetType.REPLY);
                            return new ReplyResponse.SingleResponse(
                                    reply.getId(),
                                    reply.getContent(),
                                    reply.getNickname(),
                                    reply.getCreatedAt().toString(),
                                    reply.getUpdatedAt().toString(),
                                    replyAttachmentResponses
                            );
                        })
                        .collect(Collectors.toList()),
                attachmentResponses
        );
    }

    /**
     * 단일 토론 객체를 ListResponse DTO로 변환하는 메서드
     *
     * @param discussion / 변환할 토론 객체
     * @return DiscussionResponse.ListResponse / 변환된 응답 DTO
     * @since 1.0
     * @author 최욱연
     */
    private DiscussionResponse.ListResponse toListResponse(Discussion discussion) {
        List<DiscussionAttachmentResponse> attachmentResponses = getAttachmentResponses(discussion.getId(), AttachmentTargetType.DISCUSSION);
        return new DiscussionResponse.ListResponse(
                discussion.getId(),
                discussion.getTitle(),
                discussion.getContent(),
                discussion.getUser().getNickname(),
                discussion.getViewCount(),
                discussion.isClosed() ? "종료" : "진행중",
                discussion.getCreatedAt().toString(),
                discussion.getUpdatedAt().toString(),
                attachmentResponses
        );
    }
}


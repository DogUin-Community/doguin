package com.sparta.doguin.domain.discussion.service;

import com.sparta.doguin.domain.attachment.repository.AttachmentRepository;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentDeleteService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUploadService;
import com.sparta.doguin.domain.bookmark.service.BookmarkService;
import com.sparta.doguin.domain.common.exception.DiscussionException;
import com.sparta.doguin.domain.common.response.ApiResponseDiscussionEnum;
import com.sparta.doguin.domain.discussions.dto.DiscussionRequest;
import com.sparta.doguin.domain.discussions.dto.DiscussionResponse;
import com.sparta.doguin.domain.discussions.entity.Discussion;
import com.sparta.doguin.domain.discussions.repository.DiscussionRepository;
import com.sparta.doguin.domain.discussions.service.DiscussionService;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;
import com.sparta.doguin.domain.user.service.UserService;
import com.sparta.doguin.security.AuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DiscussionServiceTest {
    @Mock
    private DiscussionRepository discussionRepository;

    @Mock
    private UserService userService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private AttachmentUploadService attachmentUploadService;

    @Mock
    private AttachmentDeleteService attachmentDeleteService;

    @Mock
    private BookmarkService bookmarkService;

    @Mock
    private AttachmentRepository attachmentRepository;

    private DiscussionService discussionService;

    private AuthUser authUser;
    private User user;
    private Discussion discussion;

    @BeforeEach
    void setUp() {
        discussionService = new DiscussionService(
                discussionRepository,
                userService,
                attachmentUploadService,
                attachmentDeleteService,
                bookmarkService,
                attachmentRepository,
                redisTemplate
        );

        authUser = new AuthUser(1L, "test@example.com", "TestUser", UserType.INDIVIDUAL, UserRole.ROLE_USER);
        user = new User(1L, "test@example.com", "password", "TestUser", UserType.INDIVIDUAL, UserRole.ROLE_USER, null, null, null, null, null);
        discussion = new Discussion("Test Title", "Test Content", user);

        LocalDateTime now = LocalDateTime.now();
        ReflectionTestUtils.setField(discussion, "id", 1L);
        ReflectionTestUtils.setField(discussion, "createdAt", now);
        ReflectionTestUtils.setField(discussion, "updatedAt", now);

        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void 토론을_정상적으로_수정한다() {
        // given
        given(discussionRepository.findByIdOrThrow(anyLong())).willReturn(discussion);

        // when
        DiscussionRequest.UpdateRequest request = new DiscussionRequest.UpdateRequest("Updated Title", "Updated Content", user.getId(), null);
        DiscussionResponse.SingleResponse response = discussionService.updateDiscussion(1L, request, authUser, null, null);

        // then
        assertNotNull(response);
        assertEquals("Updated Title", response.title());
        verify(discussionRepository).save(discussion);
    }

    @Test
    void 존재하지_않는_토론을_조회할_때_예외를_발생시킨다() {
        // given
        given(discussionRepository.findByIdOrThrow(anyLong()))
                .willThrow(new DiscussionException(ApiResponseDiscussionEnum.DISCUSSION_NOT_FOUND));

        // when
        DiscussionException exception = assertThrows(DiscussionException.class, () -> {
            discussionService.getDiscussion(1L, authUser);
        });

        // then
        assertEquals(ApiResponseDiscussionEnum.DISCUSSION_NOT_FOUND, exception.getApiResponseEnum());
    }

    @Test
    void 모든_토론을_정상적으로_조회한다() {
        // given
        Page<Discussion> discussions = new PageImpl<>(List.of(discussion));
        given(discussionRepository.findAllByOrderByClosedAscCreatedAtDesc(any(Pageable.class)))
                .willReturn(discussions);

        // when
        Page<DiscussionResponse.ListResponse> responsePage = discussionService.getAllDiscussions(Pageable.unpaged());

        // then
        assertNotNull(responsePage);
        assertFalse(responsePage.isEmpty());
        assertEquals(1, responsePage.getTotalElements());
        verify(discussionRepository).findAllByOrderByClosedAscCreatedAtDesc(any(Pageable.class));
    }

    @Test
    void 토론을_삭제할_때_정상적으로_삭제한다() {
        // given
        given(discussionRepository.findByIdOrThrow(anyLong())).willReturn(discussion);

        // when
        discussionService.deleteDiscussion(1L, authUser);

        // then
        verify(discussionRepository).delete(discussion);
    }
}

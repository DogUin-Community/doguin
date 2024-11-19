package com.sparta.doguin.domain.discussion.service;

import com.sparta.doguin.domain.attachment.repository.AttachmentRepository;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentDeleteService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUploadService;
import com.sparta.doguin.domain.bookmark.service.BookmarkService;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {
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

        given(discussionRepository.save(any(Discussion.class))).willAnswer(invocation -> {
            Discussion savedDiscussion = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedDiscussion, "id", 1L);
            ReflectionTestUtils.setField(savedDiscussion, "createdAt", now);
            ReflectionTestUtils.setField(savedDiscussion, "updatedAt", now);
            return savedDiscussion;
        });

        given(discussionRepository.findByIdOrThrow(1L)).willReturn(discussion);

        given(redisTemplate.opsForValue()).willReturn(valueOperations);
    }

    @Test
    void 토론을_정상적으로_생성한다() {
        // given
        DiscussionRequest.CreateRequest request = new DiscussionRequest.CreateRequest("Test Title", "Test Content", user.getId(), null);
        given(userService.findById(authUser.getUserId())).willReturn(user);

        // when
        DiscussionResponse.SingleResponse response = discussionService.createDiscussion(authUser, null, request).getData();

        // then
        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals("Test Title", response.title());
        verify(discussionRepository).save(any(Discussion.class)); // 기존 검증
        verify(discussionRepository).findByIdOrThrow(1L); // 추가된 검증
    }
}

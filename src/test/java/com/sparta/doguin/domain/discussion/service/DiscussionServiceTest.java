package com.sparta.doguin.domain.discussion.service;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DiscussionServiceTest {
    @Mock
    private DiscussionRepository discussionRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private DiscussionService discussionService;

    private AuthUser authUser;
    private User user;
    private Discussion discussion;

    @BeforeEach
    void setUp() {
        authUser = new AuthUser(1L, "test@example.com", "TestUser", UserType.INDIVIDUAL, UserRole.ROLE_USER);
        user = new User(
                1L,
                "test@example.com",
                "password",
                "TestUser",
                UserType.INDIVIDUAL,
                UserRole.ROLE_USER,
                null,
                null,
                null,
                null,
                null
        );

        discussion = new Discussion("Test Title", "Test Content", user);

        ReflectionTestUtils.setField(discussion, "id", 1L);
    }

    @Test
    void 토론을_정상적으로_생성한다() {
        // given
        DiscussionRequest.CreateRequest request = new DiscussionRequest.CreateRequest("Test Title", "Test Content", user.getId(), null);
        given(userService.findById(authUser.getUserId())).willReturn(user);
        given(discussionRepository.save(any(Discussion.class))).willReturn(discussion);

        // when
        DiscussionResponse.SingleResponse response = discussionService.createDiscussion(authUser, null, request).getData();

        // then
        assertNotNull(response);
        assertEquals("Test Title", response.title());
        assertEquals("Test Content", response.content());
        verify(discussionRepository).save(any(Discussion.class));
    }

    @Test
    void 존재하지_않는_토론을_조회할_때_예외를_발생시킨다() {
        // given
        given(discussionRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        DiscussionException exception = assertThrows(DiscussionException.class, () -> {
            discussionService.getDiscussion(1L, authUser);
        });

        // then
        assertEquals(ApiResponseDiscussionEnum.DISCUSSION_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 모든_토론을_정상적으로_조회한다() {
        // given
        Page<Discussion> discussions = new PageImpl<>(List.of(discussion));
        given(discussionRepository.findAllByOrderByClosedAscCreatedAtDesc(any(Pageable.class))).willReturn(discussions);

        // when
        Page<DiscussionResponse.ListResponse> responsePage = discussionService.getAllDiscussions(Pageable.unpaged());

        // then
        assertNotNull(responsePage);
        assertFalse(responsePage.isEmpty());
        assertEquals(1, responsePage.getTotalElements());
    }

    @Test
    void 토론을_정상적으로_수정한다() {
        // given
        DiscussionRequest.UpdateRequest request = new DiscussionRequest.UpdateRequest("Updated Title", "Updated Content", user.getId(), null);
        given(discussionRepository.findById(anyLong())).willReturn(Optional.of(discussion));
        given(userService.findById(authUser.getUserId())).willReturn(user);

        // when
        DiscussionResponse.SingleResponse response = discussionService.updateDiscussion(1L, request, authUser, null, null);

        // then
        assertNotNull(response);
        assertEquals("Updated Title", response.title());
        verify(discussionRepository).save(discussion);
    }

    @Test
    void 토론을_삭제할_때_정상적으로_삭제한다() {
        // given
        given(discussionRepository.findById(anyLong())).willReturn(Optional.of(discussion));

        // when
        discussionService.deleteDiscussion(1L, authUser);

        // then
        verify(discussionRepository).delete(discussion);
    }
}

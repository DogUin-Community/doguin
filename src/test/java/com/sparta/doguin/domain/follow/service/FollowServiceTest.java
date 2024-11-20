package com.sparta.doguin.domain.follow.service;

import com.sparta.doguin.domain.common.exception.UserException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseFollowEnum;
import com.sparta.doguin.domain.follow.dto.FollowResponse;
import com.sparta.doguin.domain.follow.entity.Follow;
import com.sparta.doguin.domain.follow.repository.FollowRepository;
import com.sparta.doguin.domain.setup.DataUtil;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {

    @Mock
    FollowRepository followRepository;

    @Mock
    UserService userService;

    @InjectMocks
    FollowService followService;

    User user1;
    User user2;

    @BeforeEach
    void setUp() {
        user1 = DataUtil.user1();
        user2 = DataUtil.user2();
    }

    @Nested
    class FollowTest {
        @Test
        @DisplayName("팔로우 추가 성공")
        void follow_success() {
            // given
            given(userService.findById(user1.getId())).willReturn(user1);
            given(userService.findByEmail(user2.getEmail())).willReturn(user2);
            given(followRepository.existsByFollowerIdAndFollowedId(user1.getId(), user2.getId())).willReturn(false);

            // when
            ApiResponse<FollowResponse.Follow> actual = followService.follow(user1.getId(), user2.getEmail());

            // then
            assertEquals(ApiResponseFollowEnum.FOLLOW_SUCCESS.getCode(), actual.getCode());
            assertEquals(ApiResponseFollowEnum.FOLLOW_SUCCESS.getMessage(), actual.getMessage());
            Mockito.verify(followRepository).save(Mockito.any(Follow.class)); // 팔로우 저장 확인
        }

        @Test
        @DisplayName("팔로우 추가 실패 - 이미 팔로우 중")
        void follow_alreadyFollowing() {
            // given
            given(userService.findById(user1.getId())).willReturn(user1);
            given(userService.findByEmail(user2.getEmail())).willReturn(user2);
            given(followRepository.existsByFollowerIdAndFollowedId(user1.getId(), user2.getId())).willReturn(true);

            // when & then
            assertThrows(UserException.class, () -> followService.follow(user1.getId(), user2.getEmail()));
        }
    }

    @Nested
    class GetFollowersTest {
        @Test
        @DisplayName("나를 팔로우한 사용자 목록 조회 성공")
        void getFollowers_success() {
            // given
            Follow follow1 = new Follow(user2, user1);
            List<Follow> followers = List.of(follow1);
            given(followRepository.findByFollowedId(user1.getId())).willReturn(followers);

            // when
            ApiResponse<List<FollowResponse.Follow>> actual = followService.getFollowers(user1.getId());

            // then
            assertEquals(ApiResponseFollowEnum.FOLLOW_GET_SUCCESS.getCode(), actual.getCode());
            assertEquals(1, actual.getData().size()); // 나를 팔로우한 사용자 수 확인
            assertEquals(user2.getId(), actual.getData().get(0).userId()); // userId로 접근
        }
    }

    @Nested
    class GetFollowedUsersTest {
        @Test
        @DisplayName("내가 팔로우한 사용자 목록 조회 성공")
        void getFollowedUsers_success() {
            // given
            Follow follow1 = new Follow(user1, user2);
            List<Follow> followedUsers = List.of(follow1);
            given(followRepository.findByFollowerId(user1.getId())).willReturn(followedUsers);

            // when
            ApiResponse<List<FollowResponse.Follow>> actual = followService.getFollowedUsers(user1.getId());

            // then
            assertEquals(ApiResponseFollowEnum.FOLLOW_GET_SUCCESS.getCode(), actual.getCode());
            assertEquals(1, actual.getData().size()); // 내가 팔로우한 사용자 수 확인
            assertEquals(user2.getId(), actual.getData().get(0).userId()); // userId로 접근
        }
    }

    @Nested
    class UnfollowTest {
        @Test
        @DisplayName("팔로우 해지 성공")
        void unfollow_success() {
            // given
            given(userService.findById(user1.getId())).willReturn(user1);
            given(userService.findByEmail(user2.getEmail())).willReturn(user2);
            given(followRepository.existsByFollowerIdAndFollowedId(user1.getId(), user2.getId())).willReturn(true);

            // when
            ApiResponse<FollowResponse.Follow> actual = followService.unfollow(user1.getId(), user2.getEmail());

            // then
            assertEquals(ApiResponseFollowEnum.UNFOLLOW_SUCCESS.getCode(), actual.getCode());
            Mockito.verify(followRepository).deleteByFollowerIdAndFollowedId(user1.getId(), user2.getId()); // 팔로우 해지 확인
        }

        @Test
        @DisplayName("팔로우 해지 실패 - 팔로우 관계 없음")
        void unfollow_notFollowing() {
            // given
            given(userService.findById(user1.getId())).willReturn(user1);
            given(userService.findByEmail(user2.getEmail())).willReturn(user2);
            given(followRepository.existsByFollowerIdAndFollowedId(user1.getId(), user2.getId())).willReturn(false);

            // when & then
            assertThrows(UserException.class, () -> followService.unfollow(user1.getId(), user2.getEmail()));
        }
    }

    @Nested
    class GetFollowStatsTest {

        @Test
        @DisplayName("나를 팔로우한 사람들의 수 조회 성공")
        void getFollowedCount_success() {
            // given
            given(followRepository.findByFollowedId(user1.getId())).willReturn(List.of(new Follow(user2, user1)));

            // when
            long followedCount = followService.getFollowedCount(user1.getId());

            // then
            assertEquals(1, followedCount); // 나를 팔로우한 사람 수 확인
        }

        @Test
        @DisplayName("내가 팔로우한 사람들의 수 조회 성공")
        void getFollowerCount_success() {
            // given
            given(followRepository.findByFollowerId(user1.getId())).willReturn(List.of(new Follow(user1, user2)));

            // when
            long followerCount = followService.getFollowerCount(user1.getId());

            // then
            assertEquals(1, followerCount); // 내가 팔로우한 사람 수 확인
        }

        @Test
        @DisplayName("나를 팔로우한 사용자 목록 조회 성공")
        void getFollowerList_success() {
            // given
            Follow follow1 = new Follow(user2, user1);
            given(followRepository.findByFollowedId(user1.getId())).willReturn(List.of(follow1));

            // when
            List<FollowResponse.Follow> actual = followService.getFollowerList(user1.getId());

            // then
            assertEquals(1, actual.size()); // 목록의 크기 확인
            assertEquals(user2.getId(), actual.get(0).userId()); // userId 확인
            assertEquals(user2.getEmail(), actual.get(0).email()); // email 확인
        }

        @Test
        @DisplayName("나를 팔로우한 사용자 목록이 비었을 때 처리 성공")
        void getFollowerList_empty() {
            // given
            given(followRepository.findByFollowedId(user1.getId())).willReturn(List.of());

            // when
            List<FollowResponse.Follow> actual = followService.getFollowerList(user1.getId());

            // then
            assertEquals(0, actual.size()); // 비어 있는 목록 확인
        }
    }

}

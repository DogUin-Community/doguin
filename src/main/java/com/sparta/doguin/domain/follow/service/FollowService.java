package com.sparta.doguin.domain.follow.service;

import com.sparta.doguin.domain.common.exception.UserException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseFollowEnum;
import com.sparta.doguin.domain.follow.dto.FollowResponse;
import com.sparta.doguin.domain.follow.entity.Follow;
import com.sparta.doguin.domain.follow.repository.FollowRepository;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;

    /**
     * 팔로우 추가 메서드
     *
     * @param followerId    로그인한 사용자의 ID
     * @param followedEmail 팔로우할 사용자의 이메일
     * @return ApiResponse<String> 팔로우 성공 메시지
     * @throws UserException 이미 팔로우 중이거나 유저가 존재하지 않는 경우 예외 처리
     * @since 1.1
     * @author 황윤서
     */
    @Transactional
    public ApiResponse<FollowResponse.Follow> follow(Long followerId, String followedEmail) {
        User follower = userService.findById(followerId);
        User followed = userService.findByEmail(followedEmail);

        // 이미 팔로우 중인지 확인
        if (followRepository.existsByFollowerIdAndFollowedId(follower.getId(), followed.getId())) {
            throw new UserException(ApiResponseFollowEnum.ALREADY_FOLLOWING);
        }

        // 팔로우 추가
        Follow follow = new Follow(follower, followed);
        followRepository.save(follow);

        return ApiResponse.of(ApiResponseFollowEnum.FOLLOW_SUCCESS);
    }

    /**
     * 나를 팔로우한 사용자 목록 조회 메서드
     *
     * @param userId 나를 팔로우한 사용자 목록을 조회할 사용자의 ID
     * @return ApiResponse<List<Long>> 나를 팔로우한 사용자 ID 목록
     * @since 1.1
     * @author 황윤서
     */
    @Transactional(readOnly = true)
    public ApiResponse<List<FollowResponse.Follow>> getFollowers(Long userId) {
        // 나를 팔로우한 사용자 ID 목록 조회
        List<FollowResponse.Follow> followerUserIds = followRepository.findByFollowedId(userId)
                .stream()
                .map(follow -> new FollowResponse.Follow(follow.getFollower().getId(), follow.getFollower().getEmail()))
                .collect(Collectors.toList());

        return ApiResponse.of(ApiResponseFollowEnum.FOLLOW_GET_SUCCESS, followerUserIds);
    }

    /**
     * 팔로우한 사용자 목록 조회 메서드(내가 팔로우한 사용자 목록)
     *
     * @param userId 팔로우한 사용자 목록을 조회할 사용자의 ID
     * @return ApiResponse<List<Long>> 팔로우한 사용자 ID 목록
     * @since 1.1
     * @author 황윤서
     */
    @Transactional(readOnly = true)
    public ApiResponse<List<FollowResponse.Follow>> getFollowedUsers(Long userId) {
        // 팔로우한 사용자 ID 목록 조회
        List<FollowResponse.Follow> followedUserIds = followRepository.findByFollowerId(userId)
                .stream()
                .map(follow -> new FollowResponse.Follow(follow.getFollowed().getId(), follow.getFollowed().getEmail()))
                .collect(Collectors.toList());

        // 목록 반환
        return ApiResponse.of(ApiResponseFollowEnum.FOLLOW_GET_SUCCESS, followedUserIds);
    }

    /**
     * 팔로우 해지 요청을 처리하는 메서드
     *
     * @param followerId 팔로우를 하는 사용자의 ID
     * @param followedEmail 팔로우를 당한 사용자의 이메일
     * @return ApiResponse<String> 팔로우 해지 성공 메시지
     * @throws UserException 팔로우 관계가 존재하지 않는 경우 예외 처리
     * @since 1.1
     * @author 황윤서
     */
    @Transactional
    public ApiResponse<FollowResponse.Follow> unfollow(Long followerId, String followedEmail) {
        User follower = userService.findById(followerId);
        User followed = userService.findByEmail(followedEmail);

        // 팔로우 관계가 존재하는지 확인
        if (!followRepository.existsByFollowerIdAndFollowedId(follower.getId(), followed.getId())) {
            throw new UserException(ApiResponseFollowEnum.FOLLOW_NOT_FOUND);
        }

        // 팔로우 해지
        followRepository.deleteByFollowerIdAndFollowedId(follower.getId(), followed.getId());

        return ApiResponse.of(ApiResponseFollowEnum.UNFOLLOW_SUCCESS);
    }

    // 나를 팔로우한 사람들의 수
    public long getFollowedCount(Long userId) {
        return followRepository.findByFollowedId(userId).size();
    }

    // 내가 팔로우한 사람들의 수
    public long getFollowerCount(Long userId) {
        return followRepository.findByFollowerId(userId).size();
    }

    @Transactional(readOnly = true)
    public List<FollowResponse.Follow> getFollowerList(Long userId) {
        // 나를 팔로우한 사용자 ID 목록 조회
        List<FollowResponse.Follow> followerUserIds = followRepository.findByFollowedId(userId)
                .stream()
                .map(follow -> new FollowResponse.Follow(follow.getFollower().getId(), follow.getFollower().getEmail()))
                .collect(Collectors.toList());

        return followerUserIds;
    }
}
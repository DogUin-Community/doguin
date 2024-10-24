package com.sparta.doguin.domain.follow.repository;

import com.sparta.doguin.domain.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    // 팔로우 중인 관계가 있는지 확인
    boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);

    // 나를 팔로우한 사람들의 목록 조회
    List<Follow> findByFollowedId(Long followedId);

    // 특정 사용자가 팔로우한 사람들의 목록 조회
    List<Follow> findByFollowerId(Long followedId);

    // 팔로우 해제
    void deleteByFollowerIdAndFollowedId(Long followerId, Long followedId);
}
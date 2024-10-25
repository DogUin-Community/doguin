package com.sparta.doguin.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViewTrackingService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 게시물를 조회한 유저를 set에 저장해 어뷰징을 방지한 일일 조회수 누적기
     *
     * @param boardId 조회 대상 일반 게시물의 id
     * @param userId 로그인 한 계정
     * @author 김창민
     * @since 1.0
     */
    public void trackUserView(Long boardId, Long userId) {
        String key = "todayView:" + boardId;
        redisTemplate.opsForSet().add(key, userId.toString());
    }

    /**
     * 일일 조회수 반환기
     *
     * @param boardId 조회 대상 일반 게시물의 id
     * @author 김창민
     * @return 일일 조회수
     * @since 1.0
     */
    public Long getDailyUniqueViewCount(Long boardId) {
        String key = "todayView:" + boardId;
        return redisTemplate.opsForSet().size(key); // Set 크기 반환
    }

    /**
     * 입력되는 boardId에 해당하는 게시물의 일일조회수 초기화
     *
     * @param boardId 조회 대상 일반 게시물의 id
     * @author 김창민
     * @since 1.0
     */
    public void resetDailyViewCount(Long boardId) {
        String key = "todayView:" + boardId;
        redisTemplate.delete(key); // 자정에 Set 삭제
    }
}

package com.sparta.doguin.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ViewTrackingService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void trackUserView(Long boardId, Long userId) {
        String key = "todayView:" + boardId;
        redisTemplate.opsForSet().add(key, userId.toString());
    }

    public Long getDailyUniqueViewCount(Long boardId) {
        String key = "todayView:" + boardId;
        return redisTemplate.opsForSet().size(key); // Set 크기 반환
    }

    public void resetDailyViewCount(Long boardId) {
        String key = "todayView:" + boardId;
        redisTemplate.delete(key); // 자정에 Set 삭제
    }
}

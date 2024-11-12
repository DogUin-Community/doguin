package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.board.repository.BoardRepository;
import com.sparta.doguin.domain.common.exception.HandleNotFound;
import com.sparta.doguin.domain.common.response.ApiResponseBoardEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PopularService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final BoardRepository boardRepository;

    /**
     * 한 게시글의 조회수. 어뷰징 방지를 위해 한 시간당 한 명만 유효하게 처리
     *
     * @param boardId 조회 대상 일반 게시물의 id
     * @param userId  로그인 한 계정
     */
    public void trackUserView(Long boardId, Long userId) {
        String hourKey = "hourView:" + boardId;
        redisTemplate.opsForSet().add(hourKey, userId.toString());
    }

    /**
     * 지난 1시간의 누적 조회인을 하루 조회인에 업로드하고 리셋
     */
    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void updateTodayView() {
        Set<String> keysToUpdate = redisTemplate.keys("hourView:*");
        if (keysToUpdate != null && !keysToUpdate.isEmpty()) {
            for (String val : keysToUpdate) {
                String[] keys = val.split(":");
                String todayKey = "todayView:" + keys[1];

                String todayViewStr = (String) redisTemplate.opsForValue().get(todayKey);
                Long todayView = (todayViewStr != null) ? Long.parseLong(todayViewStr) : 0L;

                long hourView = redisTemplate.opsForSet().size(val);

                Long totalView = todayView + hourView;

                redisTemplate.opsForValue().set(todayKey, Long.toString(totalView));
                redisTemplate.delete(val);
            }
        }
    }

    /**
     * 일일 조회수 반환기
     *
     * @param boardId 조회 대상 일반 게시물의 id
     * @return 일일 조회수
     */
    public Long getHourUniqueViewCount(Long boardId) {
        String hourKey = "hourView:" + boardId;
        Long count = (long) redisTemplate.opsForSet().members(hourKey).size();
        return count != null ? count : 0L;
    }

    /**
     * 정각에 Redis에 저장된 각 게시물의 일일 조회수를 삭제하고, 누적 조회수에 반영
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateTotalViewCounts() {
        updateTodayView();
        Set<String> keysToUpdate = redisTemplate.keys("todayView:*");
        if (keysToUpdate != null && !keysToUpdate.isEmpty()) {
            for (String val : keysToUpdate) {
                String[] keys = val.split(":");
                Board board = boardRepository.findById(Long.parseLong(keys[1]))
                        .orElseThrow(() -> new HandleNotFound(ApiResponseBoardEnum.BULLETIN_NOT_FOUND));

                String todayViewStr = (String) redisTemplate.opsForValue().get(val);
                Long todayView = (todayViewStr != null) ? Long.parseLong(todayViewStr) : 0L;

                Long totalView = board.getView();
                board.changeTotalViewCount(totalView, todayView);

                redisTemplate.delete(val);
            }
        }
        log.info("누적 조회수 업로드 완료");
    }

    /**
     * 상위 인기 게시글 조회
     *
     * @return 인기 게시글 ID 목록
     */
    public Set<Long> viewPopularBoardList() {
        Set<Object> popularBoardIds = redisTemplate.opsForZSet().reverseRange("popularBoard", 0, 2);
        return popularBoardIds.stream()
                .map(id -> Long.parseLong(id.toString()))
                .collect(Collectors.toSet());
    }


    /**
     * 한 시간마다 상위 3개의 인기 게시글을 업데이트
     */
    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void updatePopularBoard() {
        PriorityQueue<Long[]> views = new PriorityQueue<>((a, b) -> Long.compare(a[1], b[1]));
        String popularBoard = "popularBoard";

        // 1시간 동안 수집된 조회수를 이용해 인기글 순위를 계산
        Set<String> keysToUpdate = redisTemplate.keys("hourView:*");
        Set<Object> currentPopularBoard = redisTemplate.opsForZSet().range(popularBoard, 0, -1);

        if (keysToUpdate != null && !keysToUpdate.isEmpty()) {
            for (String key : keysToUpdate) {
                String[] keys = key.split(":");
                Long boardId = Long.parseLong(keys[1]);

                // 기존 인기글에 포함되지 않은 게시글만 고려
                if (currentPopularBoard == null || !currentPopularBoard.contains(boardId.toString())) {
                    long hourView = redisTemplate.opsForSet().size(key);
                    views.offer(new Long[]{boardId, hourView});

                    // 상위 3개의 게시글만 유지
                    if (views.size() > 3) {
                        views.poll();
                    }
                }

                // 수집한 hourView 데이터를 삭제
                redisTemplate.delete(key);
            }
        }

        // 새로 선정된 인기 게시글을 `popularBoard`에 추가
        for (Long[] viewData : views) {
            redisTemplate.opsForZSet().add(popularBoard, viewData[0].toString(), viewData[1]);
        }

        log.info("인기 게시글 업데이트 완료");
    }
}

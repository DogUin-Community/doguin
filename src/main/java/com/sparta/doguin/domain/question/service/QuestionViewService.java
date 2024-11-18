package com.sparta.doguin.domain.question.service;

import com.sparta.doguin.domain.common.exception.QuestionException;
import com.sparta.doguin.domain.common.response.ApiResponseQuestionEnum;
import com.sparta.doguin.domain.question.entity.Question;
import com.sparta.doguin.domain.question.repository.QuestionRepository;
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
public class QuestionViewService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final QuestionRepository questionRepository;

    // 조회수 증가 메서드
    public void trackUserView(Long questionId, Long userId) {
        String key = "hourView:" + questionId;
        redisTemplate.opsForSet().add(key, userId.toString());
    }

    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void updateTodayView() {
        // Redis에서 hourView:로 시작하는 모든 키를 가져옴
        Set<String> keysToUpdate = redisTemplate.keys("hourView*");

        // hourView:로 시작하는 키가 존재하는지 확인 없으면 종료
        if (keysToUpdate != null && !keysToUpdate.isEmpty()) {
            // Redis에 존재하는 1시간 조회 키 순회
            for (String val : keysToUpdate) {
                String[] keys = val.split(","); // questionId를 파싱
                String todayKey = "todayView:" + keys[1];

                // 오늘 누적 조회수 가져오기
                String todayViewStr = (String) redisTemplate.opsForValue().get(todayKey);
                // 조회수가 있는 경우: 문자열 값을 숫자로 변환해서 저장
                // 조회수가 없는 경우: 0으로 초기화
                Long todayView = (todayViewStr != null) ? Long.parseLong(todayViewStr) : 0L;

                // 한시간 누적 조회수 가져오기
                long hourView = redisTemplate.opsForSet().size(val);

                // 일일 누적 조회수 + 한 시간 동안의 조회수
                Long totalView = todayView + hourView;

                // 오늘 조회수 업데이트
                redisTemplate.opsForValue().set(todayKey, Long.toString(totalView));
                redisTemplate.delete(val);
            }
        }
    }

    public Long getHourUniqueViewCount (Long questionId) {
        String hourKey = "hourView:" + questionId;
        Set<Object> members = redisTemplate.opsForSet().members(hourKey);

        return (members != null) ? (long) members.size() : 0L;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateTotalViewCounts() {
        updateTodayView();
        Set<String> keysToUpdate = redisTemplate.keys("todayView*");
        if (keysToUpdate != null && !keysToUpdate.isEmpty()) {
            for (String val : keysToUpdate) {
                String[] keys = val.split(":");
                Question question = questionRepository.findById(Long.parseLong(keys[1]))
                        .orElseThrow(() -> new QuestionException(ApiResponseQuestionEnum.QUESTION_NOT_FOUND));

                String todayViewStr = (String) redisTemplate.opsForValue().get(val);
                Long todayView = (todayViewStr != null) ? Long.parseLong(todayViewStr) : 0L;

                Long totalView = question.getView();
                question.changeTotalViewCount(totalView, todayView);

                redisTemplate.delete(val);
            }
        }
    }

    public Set<Long> viewPopularQuestionList() {
        Set<Object> popularQuestionIds = redisTemplate.opsForZSet().reverseRange("popularQuestion", 0, 2);
        return popularQuestionIds.stream()
                .map(id -> Long.parseLong(id.toString()))
                .collect(Collectors.toSet());
    }

    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void updatePopularQuestion() {
        PriorityQueue<Long[]> views = new PriorityQueue<>((a, b) -> Long.compare(a[1], b[1]));
        String popularQuestion = "popularQuestion";

        Set<String> keysToUpdate = redisTemplate.keys("hourView:*");
        Set<Object> currentPopularQuestion = redisTemplate.opsForZSet().range(popularQuestion, 0, -1);

        if (keysToUpdate != null && !keysToUpdate.isEmpty()) {
            for (String key : keysToUpdate) {
                String[] keys = key.split(":");
                Long boardId = Long.parseLong(keys[1]);

                // 기존 인기글에 포함되지 않은 게시글만 고려
                if (currentPopularQuestion == null || !currentPopularQuestion.contains(boardId.toString())) {
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

        for (Long[] val : views) {
            redisTemplate.opsForZSet().add(popularQuestion, val[0].toString(), val[1]);
        }
    }
}

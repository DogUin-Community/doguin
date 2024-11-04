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

        // Sorted Set에 조회수 증가 적용 (쓰기 캐싱)
        redisTemplate.opsForZSet().incrementScore("questionRank", questionId.toString(), 1);
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
        Long count = (long) redisTemplate.opsForSet().members(hourKey).size();
        // count가 null이면 0L 반환
        return count != null ? count : 0L;
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
            }
        }
    }

    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void updatePopularQuestion() {
        PriorityQueue<Long[]> views = new PriorityQueue<>((a, b) -> Long.compare(a[1], b[1]));
        String popularQuestion = "popularQuestion";

        Set<String> popularQuestions = redisTemplate.keys(popularQuestion);
        Set<String> keysToUpdate = redisTemplate.keys("hourView:*");

        if (keysToUpdate != null && !keysToUpdate.isEmpty()) {
            for (String val : keysToUpdate) {
                String[] keys = val.split(":");
                Long questionId = Long.parseLong(keys[1]);

                if (popularQuestions != null && popularQuestions.contains(popularQuestion + questionId)) {
                    continue;
                }

                Long hourView = redisTemplate.opsForSet().size(val);
                views.offer(new Long[]{questionId, hourView});

                if (views.size() > 5) {
                    views.poll();
                }
            }
        }

        for (Long[] val : views) {
            redisTemplate.opsForSet().add(popularQuestion, val[0]);
        }
    }

    public Set viewPopularQuestionList() {
        return redisTemplate.opsForSet().members("popularQuestion");
    }




































//    public void popularQuestion() {
//        String questionBoard = "questionBoard";
//
//        // 기존의 인기 게시글 삭제
//        redisTemplate.delete(questionBoard);
//
//        // 상위 5개의 게시글 조회
//        Set<Object> questionRankId = redisTemplate.opsForZSet().reverseRange("questionRank", 0, 4);
//
//        if (questionRankId != null && !questionRankId.isEmpty()) {
//            // 상위 5개 게시글이 있을 때 실행
//            for (Object questionId : questionRankId) {
//                // questionId의 조회수를 가져옴
//                Double score = redisTemplate.opsForZSet().score("questionRank", questionId);
//
//                // 조회수가 null이 아니면 questionBoard에 저장
//                if (score != null) {
//                    redisTemplate.opsForZSet().add("questionBoard", questionId, score);
//                }
//            }
//        }
//    }
//
//    public Set viewPopularQuestionList() {
//        return redisTemplate.opsForZSet().reverseRange("questionBoard", 0, 4);
//    }




}

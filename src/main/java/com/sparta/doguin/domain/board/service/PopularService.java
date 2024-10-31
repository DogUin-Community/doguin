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

@Service
@Slf4j
@RequiredArgsConstructor
public class PopularService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final BoardRepository boardRepository;

    /**
     * 한 게시글의 조회수. 어뷰징 방지를 위해 한시간당 한명
     *
     * @param boardId 조회 대상 일반 게시물의 id
     * @param userId 로그인 한 계정
     * @author 김창민
     * @since 1.0
     */
    public void trackUserView(Long boardId, Long userId) {
        String hourKey = "hourView:" + boardId;
        redisTemplate.opsForSet().add(hourKey, userId.toString()); // set형식으로 안에는 String형의 userId가 들어감.
    }

    /**
     * 지난 1시간의 누적 조회인을 하루 조회인에 업로드 하고 리셋
     *
     * @author 김창민
     * @since 1.0
     */
    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void updateTodayView() {
        Set<String> keysToUpdate = redisTemplate.keys("hourView:*");
        if (keysToUpdate != null && !keysToUpdate.isEmpty()) {
            for (String val : keysToUpdate) {                                      // 레디스에 존재하는 1시간 조회 키 순회
                String[] keys = val.split(":");                             // 나눠서 id 파싱
                String todayKey = "todayView:" + keys[1];                          // todayKey를 생성합니다.

                // 오늘 누적 조회수 가져오기
                String todayViewStr = (String) redisTemplate.opsForValue().get(todayKey);
                Long todayView = (todayViewStr != null) ? Long.parseLong(todayViewStr) : 0L; // String을 Long으로 변환

                // 한 시간 누적 조회수 가져오기
                long hourView = redisTemplate.opsForSet().size(val); // long으로 가져오기

                Long totalView = todayView + hourView;

                // 오늘의 뷰 수 업데이트
                redisTemplate.opsForValue().set(todayKey, Long.toString(totalView));    // 지금까지의 누적 수 갱신
                redisTemplate.delete(val);
            }
        }
    }

    /**
     * 일일 조회수 반환기
     *
     * @param boardId 조회 대상 일반 게시물의 id
     * @author 김창민
     * @return 일일 조회수
     * @since 1.0
     */
    public Long getHourUniqueViewCount(Long boardId) {
        String hourKey = "hourView:" +boardId;
        Long count = (long) redisTemplate.opsForSet().members(hourKey).size();
        return count != null ? count : 0L;
    }

    /**
     * 정각에 레디스에 저장된 각 게시물의 일일 조회수를 삭제하고, 누적 조회수에 반영
     *
     * @author 김창민
     * @since 1.0
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

                // 오늘 전체 조회수 가져오기 (String을 Long으로 변환)
                String todayViewStr = (String) redisTemplate.opsForValue().get(val); // 오늘 전체 조회수
                Long todayView = (todayViewStr != null) ? Long.parseLong(todayViewStr) : 0L; // String을 Long으로 변환

                Long totalView = board.getView();
                board.changeTotalViewCount(totalView, todayView);
            }
        }
        log.info("누적 조회수 업로드 완료");
    }

    /**
     * 한시간 마다 인기글 업로드 3개
     *
     * @author 김창민
     * @since 1.0
     */
    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void updatePopularBoard(){
        PriorityQueue<Long[]> views = new PriorityQueue<>((a, b) -> Long.compare(a[1], b[1]));
        String popularBoard = "popularBoard";

        Set<String> popularBoards = redisTemplate.keys(popularBoard);
        Set<String> keysToUpdate = redisTemplate.keys("hourView:*");

        if (keysToUpdate != null && !keysToUpdate.isEmpty()&&popularBoards != null) {
            for(String val : keysToUpdate) {
                String[] keys = val.split(":");
                Long boardId = Long.parseLong(keys[1]);
                //이미 인기글에 있는지 체크 해야함.
                if(popularBoards.contains(popularBoard+boardId)){
                    continue;
                }

                Long hourView = redisTemplate.opsForSet().size(val);
                views.offer(new Long[]{boardId, hourView});
                if(views.size()>3){
                    views.poll();
                }
            }
        }

        //여기서 인기글로 추가
        for(Long[] val : views){
            redisTemplate.opsForSet().add(popularBoard,val[0]);
        }
    }

    public Set viewPopularBoardList(){
        return redisTemplate.opsForSet().members("popularBoard");
    }
}

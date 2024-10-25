package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ViewCountUpdater {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ViewTrackingService viewTrackingService;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
    @Transactional
    public void updateTotalViewCounts() {
        // 전체 게시물을 조회하여 조회수 업데이트
        for (Board board : boardRepository.findAll()) {
            Long dailyUniqueViews = viewTrackingService.getDailyUniqueViewCount(board.getId());
            board.setTotalViewCount(board.getView() , dailyUniqueViews);

            // 전체 조회수 업데이트 후 Redis 데이터 초기화
            viewTrackingService.resetDailyViewCount(board.getId());
            boardRepository.save(board);
        }
    }
}

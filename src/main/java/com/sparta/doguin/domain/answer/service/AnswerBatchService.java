package com.sparta.doguin.domain.answer.service;

import com.sparta.doguin.domain.answer.enums.AnswerStatus;
import com.sparta.doguin.domain.answer.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerBatchService {

    private final AnswerRepository answerRepository;

    // 2일에 한번 새벽 2시에 댓글 Batch 작업 실행
    @Scheduled(cron = "0 0 2 */2 * *")
    @Transactional
    public void deleteOldAnswer() {
        answerRepository.deleteByStatus(AnswerStatus.DELETED);
    }

}

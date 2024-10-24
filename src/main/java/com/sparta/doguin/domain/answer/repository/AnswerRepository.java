package com.sparta.doguin.domain.answer.repository;

import com.sparta.doguin.domain.answer.AnswerType;
import com.sparta.doguin.domain.answer.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Page<Answer> findAllByAnswerType(Pageable pageable, AnswerType answerType);

    // 게시글 ID로 답변 목록 조회
    Page<Answer> findByBoardId(long boardId, Pageable pageable);
}

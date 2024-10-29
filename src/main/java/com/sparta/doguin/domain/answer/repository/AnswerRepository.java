package com.sparta.doguin.domain.answer.repository;

import com.sparta.doguin.domain.answer.entity.Answer;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // 게시글 ID로 답변 목록 조회
    Page<Answer> findByBoardId(long boardId, Pageable pageable);

    // 특정 질문에 대한 답변 조회
    Page<Answer> findByQuestionId(long questionId, Pageable pageable);

    // 부모 ID로 대답변 조회
    List<Answer> findByParentId(Long parentId);

    Page<Answer> findByQuestion(Question question, Pageable pageable);

    @Query("SELECT b FROM Board b WHERE b.id = :boardId")
    Board findBoardById(@Param("boardId") Long boardId);

}

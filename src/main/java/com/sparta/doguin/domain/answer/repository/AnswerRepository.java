package com.sparta.doguin.domain.answer.repository;

import com.sparta.doguin.domain.answer.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // 게시글 ID로 답변 목록 조회
    Page<Answer> findByBoardId(long boardId, Pageable pageable);

    // 특정 질문에 대한 답변 조회
    Page<Answer> findByQuestionId(Long questionId, Pageable pageable);

    // 부모 ID로 대답변 조회
    List<Answer> findByParentId(Long parentId);

    @Query("SELECT a FROM Answer a WHERE a.question.id = :questionId")
    Optional<Answer> findFirstByQuestionId(Long questionId);

}

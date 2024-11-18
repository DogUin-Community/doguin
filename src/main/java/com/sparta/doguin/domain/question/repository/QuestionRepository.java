package com.sparta.doguin.domain.question.repository;

import com.sparta.doguin.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionRepositoryQuery {
    List<Question> findAllByUserId(long userId);

    // 답변과 대답변을 한 번에 가져옴
    @Query("SELECT q FROM Question q " + "LEFT JOIN FETCH q.answerList a " + "LEFT JOIN FETCH a.parent " + "WHERE q.id = :questionId")
    Optional<Question> findByIdWithAnswers(@Param("questionId") Long questionId);
}

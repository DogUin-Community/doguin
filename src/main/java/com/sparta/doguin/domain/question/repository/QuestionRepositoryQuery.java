package com.sparta.doguin.domain.question.repository;

import com.sparta.doguin.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionRepositoryQuery {
    Page<Question> search(String title, String content, Pageable pageable);
}

package com.sparta.doguin.domain.question.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.doguin.domain.question.entity.QQuestion;
import com.sparta.doguin.domain.question.entity.Question;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class QuestionRepositoryQueryImpl implements QuestionRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    QQuestion question = QQuestion.question;

    @Override
    public Page<Question> search(String title, String content, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        // 제목 검색 조건
        if (title != null && !title.isEmpty()) {
            builder.and(question.title.contains(title));
        }

        // 본문 검색 조건
        if (content != null && !content.isEmpty()) {
            builder.and(question.content.contains(content));
        }

        List<Question> questionList = queryFactory
                .select(
                        Projections.constructor(
                                Question.class,
                                question.id,
                                question.title,
                                question.content
                        )
                )
                .distinct() // 중복제거
                .from(question)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(questionList, pageable, questionList.size());
    }
}

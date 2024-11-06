package com.sparta.doguin.domain.discussions.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.doguin.domain.discussions.entity.Discussion;
import com.sparta.doguin.domain.discussions.entity.QDiscussion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DiscussionRepositoryQueryImpl implements DiscussionRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Discussion> search(String title, String content, String nickname, Pageable pageable) {
        QDiscussion qDiscussion = QDiscussion.discussion;
        BooleanBuilder builder = new BooleanBuilder();

        if (title != null && !title.isEmpty()) {
            builder.and(qDiscussion.title.containsIgnoreCase(title));
        }
        if (content != null && !content.isEmpty()) {
            builder.and(qDiscussion.content.containsIgnoreCase(content));
        }
        if (nickname != null && !nickname.isEmpty()) {
            builder.and(qDiscussion.user.nickname.containsIgnoreCase(nickname));
        }

        List<Discussion> results = queryFactory
                .selectFrom(qDiscussion)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(qDiscussion.id.count())
                .from(qDiscussion)
                .where(builder)
                .fetchOne();

        total = (total != null) ? total : 0L;

        return new PageImpl<>(results, pageable, total);
    }
}

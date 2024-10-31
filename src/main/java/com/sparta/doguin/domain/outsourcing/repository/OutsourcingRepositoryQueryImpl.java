package com.sparta.doguin.domain.outsourcing.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
import com.sparta.doguin.domain.outsourcing.entity.QOutsourcing;
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
public class OutsourcingRepositoryQueryImpl implements OutsourcingRepositoryQuery {
    private final JPAQueryFactory q;

    QOutsourcing qOutsourcing = QOutsourcing.outsourcing;


    @Override
    public Page<Outsourcing> search(String title, String content, String nickname, Pageable pageable) {
        BooleanBuilder b = new BooleanBuilder();


        if (title != null && !title.trim().isEmpty()) {
            b.and(qOutsourcing.title.startsWith(title));
        }

        if (content != null && !content.trim().isEmpty()) {
            b.and(qOutsourcing.content.startsWith(content));
        }

        if (nickname != null && !nickname.trim().isEmpty()) {
            b.and(qOutsourcing.user.nickname.startsWith(nickname));
        }

        List<Outsourcing> outsourcingList = q
                .select(
                        Projections.constructor(
                                Outsourcing.class,
                                qOutsourcing.id,
                                qOutsourcing.user,
                                qOutsourcing.title,
                                qOutsourcing.content,
                                qOutsourcing.preferential,
                                qOutsourcing.work_type,
                                qOutsourcing.price,
                                qOutsourcing.recruit_start_date,
                                qOutsourcing.recruit_end_date,
                                qOutsourcing.work_start_date,
                                qOutsourcing.work_end_date,
                                qOutsourcing.area
                        )
                )
                .distinct()
                .from(qOutsourcing)
                .where(b)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(outsourcingList, pageable, outsourcingList.size());
    }
}

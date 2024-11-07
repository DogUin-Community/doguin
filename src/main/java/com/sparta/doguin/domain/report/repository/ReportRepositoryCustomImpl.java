package com.sparta.doguin.domain.report.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.doguin.domain.report.dto.ReportResponse;
import com.sparta.doguin.domain.report.entity.QReport;
import com.sparta.doguin.domain.report.entity.Report;
import com.sparta.doguin.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class ReportRepositoryCustomImpl implements ReportRepositoryCustom {

    private JPAQueryFactory jpaQueryFactory;
    QReport report = QReport.report;
    QUser user = QUser.user;

    public ReportRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<ReportResponse.ReportView> findAllByReporterId(Pageable pageable, Long id) {
        List<ReportResponse.ReportView> result = jpaQueryFactory
                .select(Projections.constructor(ReportResponse.ReportView.class,
                        report.id,
                        report.title,
                        report.reportee.nickname,
                        report.reportType
                ))
                .from(report)
                .innerJoin(report.reportee, user)
                .where(
                        eqReporterId(id) // 신고자 또는 신고당한 사람으로 검색
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(Wildcard.count)
                .from(report)
                .where(eqReporterId(id).or(eqReporteeId(id))) // 전체 카운트도 조건 추가
                .fetchOne();

        return new PageImpl<>(result, pageable, count);
    }
    @Override
    public Page<ReportResponse.ReportView> findAll(Pageable pageable) {
        List<ReportResponse.ReportView> result = jpaQueryFactory
                .select(Projections.constructor(ReportResponse.ReportView.class,
                        report.id,
                        report.title,
                        report.reportee.nickname,
                        report.reportType
                ))
                .from(report)
                .innerJoin(report.reportee)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(Wildcard.count)
                .from(report)
                .fetchOne();

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public Optional<Report> findByIdWithReporterId(Long reporterId, Long reporteeId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(report)
                .innerJoin(report.reportee, user).fetchJoin()
                .where(
                        eqReporterId(reporterId),
                        eqReporteeId(reporteeId))
                .fetchOne()
        );
    }

    @Override
    public Optional<ReportResponse.ReportTotalView> findCountByReporteeId(Long reporteeId) {

        Object[] result = jpaQueryFactory
                .select(report.reportee.nickname, report.count())
                .from(report)
                .where(eqReporteeId(reporteeId))
                .groupBy(report.reportee.nickname)
                .fetchOne().toArray();

        if (result != null) {
            String nickname = (String) result[0]; // 닉네임
            Long count = (Long) result[1]; // 신고 수
            ReportResponse.ReportTotalView response = new ReportResponse.ReportTotalView(nickname, count);
            return Optional.of(response);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Report> findByReporterIdAndReporteeId(Long reporterId, Long reporteeId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(report)
                .innerJoin(report.reportee, user).fetchJoin()
                .where(eqReporterId(reporterId), eqReporteeId(reporteeId))
                .fetchFirst()); // fetchFirst()로 변경하여 안전성 증가
    }


    private BooleanExpression eqReporterId(Long userId) {
        return report.reporter.id.eq(userId);
    }

    private BooleanExpression eqReporteeId(Long userId) {
        return report.reportee.id.eq(userId);
    }


}

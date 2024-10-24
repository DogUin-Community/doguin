package com.sparta.doguin.domain.report.repository;

import com.sparta.doguin.domain.report.dto.ReportResponse;
import com.sparta.doguin.domain.report.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReportRepositoryCustom  {


    Page<ReportResponse.ReportView> findAllByReporterId(Pageable pageable, Long id);


    Optional<Report> findByIdWithReporterId(Long reporterId, Long reporteeId);

    Optional<ReportResponse.ReportTotalView> findCountByReporteeId(Long reporteeId);

    Optional<Report> findByReporterIdAndReporteeId(Long id, Long aLong);
}

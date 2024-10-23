package com.sparta.doguin.domain.report.repository;

import com.sparta.doguin.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report,Long>, ReportRepositoryCustom {

}

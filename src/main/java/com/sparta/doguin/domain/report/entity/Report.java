package com.sparta.doguin.domain.report.entity;

import com.sparta.doguin.domain.common.Timestamped;
import com.sparta.doguin.domain.report.ReportType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Report extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String report_description;
    private ReportType reportType;


    public Report(String report_description, ReportType reportType) {
        this.report_description = report_description;
        this.reportType = reportType;
    }
}

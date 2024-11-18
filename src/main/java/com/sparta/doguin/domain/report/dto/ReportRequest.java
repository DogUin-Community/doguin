package com.sparta.doguin.domain.report.dto;

public sealed interface ReportRequest permits ReportRequest.Report{
    record Report(
            String title,
            String content,
            String reporteeNickname
    )implements  ReportRequest{}
}
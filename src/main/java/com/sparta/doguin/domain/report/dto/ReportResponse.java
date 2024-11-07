package com.sparta.doguin.domain.report.dto;

import com.sparta.doguin.domain.report.ReportType;

public sealed interface ReportResponse permits ReportResponse.ReportView,ReportResponse.ReportTotalView{
    record ReportView(
            Long reporterId,
            Long reporteeId,
            String title,
            String nickName,
            ReportType reportType
    )implements  ReportResponse{}

    record ReportTotalView(
            String nickName,
            Long reportedCount
    )implements  ReportResponse{}
}
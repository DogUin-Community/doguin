package com.sparta.doguin.domain.report.entity;

import com.sparta.doguin.domain.common.Timestamped;
import com.sparta.doguin.domain.common.exception.InvalidRequestException;
import com.sparta.doguin.domain.common.response.ApiResponseReportEnum;
import com.sparta.doguin.domain.report.ReportType;
import com.sparta.doguin.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import static com.sparta.doguin.domain.report.ReportType.REPORT_NOT_CONFIRMED;

@Entity
@Getter
@NoArgsConstructor
@BatchSize(size = 10)
public class Report extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String reportDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportee_id")
    private User reportee;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ReportType reportType;


    public Report(String title, String reportDescription, User reporter, User reportee, ReportType reportType) {
        this.title = title;
        this.reportDescription = reportDescription;
        this.reporter = reporter;
        this.reportee = reportee;
        this.reportType = reportType;
    }

    public void changeReportType(ReportType reportType){
        if(!this.reportType.equals(REPORT_NOT_CONFIRMED)){
            throw new InvalidRequestException(ApiResponseReportEnum.REPORT_TYPE_WRONG);
        }
        this.reportType = reportType;
    }
}



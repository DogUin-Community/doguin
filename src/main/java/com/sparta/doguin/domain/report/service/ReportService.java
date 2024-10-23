package com.sparta.doguin.domain.report.service;

import com.sparta.doguin.domain.common.exception.HandleNotFound;
import com.sparta.doguin.domain.common.exception.InvalidRequestException;
import com.sparta.doguin.domain.common.response.ApiResponseReportEnum;
import com.sparta.doguin.domain.report.dto.ReportRequest;
import com.sparta.doguin.domain.report.dto.ReportResponse;
import com.sparta.doguin.domain.report.entity.Report;
import com.sparta.doguin.domain.report.repository.ReportRepository;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.service.AuthService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.doguin.domain.report.ReportType.*;

@Service
public class ReportService {

    private final AuthService authService;
    private final ReportRepository reportRepository;

    public ReportService(AuthService authService, ReportRepository repository, ReportRepository reportRepository) {
        this.authService = authService;
        this.reportRepository = reportRepository;
    }

    @Transactional
    public void report(User user, ReportRequest.Report reportRequest) {
        User reportee = authService.findById(reportRequest.reporteeId());

        if(reportRepository.findByReporterIdAndReporteeId(user.getId(),reportRequest.reporteeId()).isPresent()){
            throw new InvalidRequestException(ApiResponseReportEnum.REPORT_ALREADY_EXIST);
        }
        Report report = new Report(reportRequest.title(), reportRequest.content(), user, reportee, REPORT_NOT_CONFIRMED);
        reportRepository.save(report);
    }

    @Transactional
    public void accept(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(
                () -> new HandleNotFound(ApiResponseReportEnum.REPORT_NOT_FOUND)
        );
        report.changeReportType(REPORT_ACCEPT);
    }

    @Transactional
    public void inject(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(
                () -> new HandleNotFound(ApiResponseReportEnum.REPORT_NOT_FOUND)
        );
        report.changeReportType(REPORT_REJECT);
    }

    public Page<ReportResponse.ReportView> reportViewAll(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ReportResponse.ReportView> reports = reportRepository.findAllByReporterId(pageable,user.getId());
        return reports;
    }

    public ReportResponse.ReportView reportSearch(User user, Long reporteeId) {
        Report report = reportRepository.findByReporterIdAndReporteeId(user.getId(),reporteeId).orElseThrow(
                () -> new HandleNotFound(ApiResponseReportEnum.REPORT_NOT_FOUND)
        );

        return new ReportResponse.ReportView(report.getId(),report.getTitle(),report.getReportee().getNickname(),report.getReportType());
    }

    public ReportResponse.ReportTotalView reportTotal(Long reporteeId) {

        ReportResponse.ReportTotalView response = reportRepository.findCountByReporteeId(reporteeId).orElseThrow(
                () -> new HandleNotFound(ApiResponseReportEnum.REPORT_NOT_FOUND)
        );

        return response;
    }
}

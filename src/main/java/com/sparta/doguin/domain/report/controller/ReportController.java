package com.sparta.doguin.domain.report.controller;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseReportEnum;
import com.sparta.doguin.domain.report.dto.ReportRequest;
import com.sparta.doguin.domain.report.dto.ReportResponse;
import com.sparta.doguin.domain.report.service.ReportService;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.security.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 신고하기
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> report(@AuthenticationPrincipal AuthUser authUser, @RequestBody ReportRequest.Report reportRequest) {
        User user = User.fromAuthUser(authUser);
        reportService.report(user, reportRequest);
        return ApiResponse.of(ApiResponse.of(ApiResponseReportEnum.REPORT_RECEIVED_SUCCESS));
    }

    // 신고 승인 (admin only)
    @PutMapping("/{reportId}/accept")
    public ResponseEntity<ApiResponse<Void>> accept(@PathVariable Long reportId) {
        reportService.accept(reportId);
        return ApiResponse.of(ApiResponse.of(ApiResponseReportEnum.REPORT_ACCEPTED_SUCCESS));
    }

    // 신고 반려 (admin only)
    @PutMapping("/{reportId}/inject")
    public ResponseEntity<ApiResponse<Void>> inject(@PathVariable Long reportId) {
        reportService.reject(reportId);
        return ApiResponse.of(ApiResponse.of(ApiResponseReportEnum.REPORT_REJECTED_SUCCESS));
    }

    // 신고 내역 전체 조회 (현 서비스 이용자 기준)
    @GetMapping()
    public ResponseEntity<ApiResponse<Page<ReportResponse.ReportView>>> reportViewAll(@AuthenticationPrincipal AuthUser authUser,
                                                                                      @RequestParam(defaultValue = "1") int page,
                                                                                      @RequestParam(defaultValue = "10") int size) {
        User user = User.fromAuthUser(authUser);
        Page<ReportResponse.ReportView> responses = reportService.reportViewAll(user,page,size);
        return ApiResponse.of(ApiResponse.of(ApiResponseReportEnum.REPORT_VIEW_SUCCESS, responses));
    }

    // 신고 내역 전체 조회 (관리자 기준)
    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<Page<ReportResponse.ReportView>>> reportViewAllAdmin(@AuthenticationPrincipal AuthUser authUser,
                                                                                      @RequestParam(defaultValue = "1") int page,
                                                                                      @RequestParam(defaultValue = "10") int size) {

        Page<ReportResponse.ReportView> responses = reportService.reportViewAllAdmin(page,size);
        return ApiResponse.of(ApiResponse.of(ApiResponseReportEnum.REPORT_VIEW_SUCCESS, responses));
    }

    // 특정인 결과 확인 (현 서비스 이용자 기준)
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ReportResponse.ReportView>> reportSearch(@AuthenticationPrincipal AuthUser authUser,
                                                                               @RequestParam String reporteeNickname
                                                                               ) {
        User user = User.fromAuthUser(authUser);
        ReportResponse.ReportView responses = reportService.reportSearch(user,reporteeNickname);
        return ApiResponse.of(ApiResponse.of(ApiResponseReportEnum.REPORT_VIEW_SUCCESS, responses));
    }

    // 특정인 누적 신고수 조회 (admin only)
    @GetMapping("/total")
    public ResponseEntity<ApiResponse<ReportResponse.ReportTotalView>> reportTotal(@RequestParam String reporteeNickname) {
        ReportResponse.ReportTotalView responses = reportService.reportTotal(reporteeNickname);
        return ApiResponse.of(ApiResponse.of(ApiResponseReportEnum.REPORT_VIEW_SUCCESS, responses));
    }
}

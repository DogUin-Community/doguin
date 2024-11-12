package com.sparta.doguin.domain.report.service;

import com.sparta.doguin.domain.common.exception.HandleNotFound;
import com.sparta.doguin.domain.common.exception.InvalidRequestException;
import com.sparta.doguin.domain.common.response.ApiResponseReportEnum;
import com.sparta.doguin.domain.report.ReportType;
import com.sparta.doguin.domain.report.dto.ReportRequest;
import com.sparta.doguin.domain.report.dto.ReportResponse;
import com.sparta.doguin.domain.report.entity.Report;
import com.sparta.doguin.domain.report.repository.ReportRepository;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;
import com.sparta.doguin.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@Transactional
@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReportService reportService;

    private User reporter;
    private User reportee;
    private Report report;


    @BeforeEach
    void setUp(){
        reporter = new User(1L, "user1@gmail.com", "AAAaaa111!!!", "신고자", UserType.INDIVIDUAL, UserRole.ROLE_USER,"","","","","");
        reportee = new User(2L, "user2@gmail.com", "AAAaaa111!!!", "피의자", UserType.INDIVIDUAL, UserRole.ROLE_USER,"","","","","");

        report = new Report("신고합니다.", "아주 무시무시합니다.", reporter, reportee, ReportType.REPORT_NOT_CONFIRMED);
        ReflectionTestUtils.setField(report,"id",1L);
    }

    @Test
    @DisplayName("신고 등록 성공 테스트")
    void report() {
        ReportRequest.Report reportRequest = new ReportRequest.Report("신고합니다.", "아주 무시무시합니다.", "딸기");

//        given(userService.findById(anyLong())).willReturn(reporter);
        given(reportRepository.findByReporterIdAndReporteeNickname(anyLong(), anyString())).willReturn(Optional.empty());
        given(reportRepository.save(any(Report.class))).willReturn(report);

        reportService.report(reporter, reportRequest);

        // save 메서드가 호출되었는지 검증
        ArgumentCaptor<Report> reportCaptor = ArgumentCaptor.forClass(Report.class);
        verify(reportRepository, times(1)).save(reportCaptor.capture());

        // 저장된 Report 객체를 검증
        Report savedReport = reportCaptor.getValue();
        assertEquals("신고합니다.", savedReport.getTitle());
        assertEquals("아주 무시무시합니다.", savedReport.getReport_description());
    }

    @Test
    @DisplayName("신고 등록 실패 테스트(이미 존재)")
    void report_이미_신고해서_실패() {
        ReportRequest.Report reportRequest = new ReportRequest.Report("신고합니다.", "아주 무시무시합니다.", "딸기");

//        given(userService.findById(anyLong())).willReturn(reporter);
        given(reportRepository.findByReporterIdAndReporteeNickname(anyLong(), anyString())).willReturn(Optional.of(report));


        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> reportService.report(reporter, reportRequest));

        assertEquals(exception.getApiResponseEnum().getMessage(), ApiResponseReportEnum.REPORT_ALREADY_EXIST.getMessage());
    }

    @Test
    @DisplayName("신고 수리")
    void accept() {
        given(reportRepository.findById(1L)).willReturn(Optional.of(report));

        reportService.accept(1L);

        verify(reportRepository, times(1)).findById(1L);
        assertEquals(ReportType.REPORT_ACCEPT, report.getReportType()); // 상태가 변경되었는지 확인
    }

    @Test
    @DisplayName("신고 수리 실패(신고 내역 없음)")
    void accept_신고내역없음() {
        given(reportRepository.findById(1L)).willReturn(Optional.empty());

        HandleNotFound exception = assertThrows(HandleNotFound.class, () -> reportService.accept(1L));

        assertEquals(exception.getApiResponseEnum().getMessage(), ApiResponseReportEnum.REPORT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("신고 반려")
    void reject() {
        given(reportRepository.findById(1L)).willReturn(Optional.of(report));

        reportService.reject(1L);

        verify(reportRepository, times(1)).findById(1L);
        assertEquals(ReportType.REPORT_REJECT, report.getReportType()); // 상태가 변경되었는지 확인
    }

    @Test
    @DisplayName("신고 반려 실패(신고 내역 없음)")
    void reject_신고내역없음() {
        given(reportRepository.findById(1L)).willReturn(Optional.empty());

        HandleNotFound exception = assertThrows(HandleNotFound.class, () -> reportService.reject(1L));

        assertEquals(exception.getApiResponseEnum().getMessage(), ApiResponseReportEnum.REPORT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("신고한 내역 조회")
    void reportViewAll() {
        User reportee2 = new User(3L, "user3@gmail.com", "AAAaaa111!!!", "피의자2", UserType.INDIVIDUAL, UserRole.ROLE_USER,"","","","","");
        Report report2 = new Report("신고합니다.", "아주 무시무시합니다.", reportee, reportee2, ReportType.REPORT_NOT_CONFIRMED);
        ReflectionTestUtils.setField(report2,"id",2L);

        ReportResponse.ReportView reportView1 = new ReportResponse.ReportView(1L, report.getTitle(), reportee.getNickname(), report.getReportType());
        ReportResponse.ReportView reportView2 = new ReportResponse.ReportView(2L, report2.getTitle(), reportee2.getNickname(), report2.getReportType());

        List<ReportResponse.ReportView> mockReports = Arrays.asList(
                reportView1,reportView2
        );

        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ReportResponse.ReportView> reportPage = new PageImpl<>(mockReports, pageable, mockReports.size());

        given(reportRepository.findAllByReporterId(pageable, reporter.getId())).willReturn(reportPage);

        Page<ReportResponse.ReportView> responsePage = reportService.reportViewAll(reporter, page, size);

        assertThat(responsePage.getContent().size()).isEqualTo(2);
        assertThat(responsePage.getContent().get(0).title()).isEqualTo("신고합니다.");

    }

    @Test
    @DisplayName("특정인 신고 결과 조회")
    void reportSearch() {
        given(reportRepository.findByReporterIdAndReporteeNickname(1L, "딸기")).willReturn(Optional.of(report));

        ReportResponse.ReportView response = reportService.reportSearch(reporter, "딸기");
        assertEquals(response.nickName(), reportee.getNickname());
    }
    @Test
    @DisplayName("특정인 신고 결과 조회 실패 (내역없음)")
    void reportSearch_내역없음() {
        given(reportRepository.findByReporterIdAndReporteeNickname(1L, "딸기")).willReturn(Optional.empty());

        HandleNotFound exception = assertThrows(HandleNotFound.class, () -> reportService.reportSearch(reporter, "딸기"));

        assertEquals(exception.getApiResponseEnum().getMessage(), ApiResponseReportEnum.REPORT_NOT_FOUND.getMessage());

    }

    @Test
    @DisplayName("특정인 신고당한 수 조회")
    void reportTotal() {
        ReportResponse.ReportTotalView reportTotalView = new ReportResponse.ReportTotalView(reportee.getNickname(), 1L);
        given(reportRepository.findCountByReporteeNickname("딸기")).willReturn(Optional.of(reportTotalView));

        ReportResponse.ReportTotalView response = reportService.reportTotal("딸기");

        assertEquals(response.nickName(), reportee.getNickname());

    }
    @Test
    @DisplayName("특정인 신고당한 수 조회 실패 (내역없음)")
    void reportTotal_내역없어서_실패() {
        ReportResponse.ReportTotalView reportTotalView = new ReportResponse.ReportTotalView(reportee.getNickname(), 1L);
        given(reportRepository.findCountByReporteeNickname("딸기")).willReturn(Optional.empty());

        HandleNotFound exception = assertThrows(HandleNotFound.class, () -> reportService.reportTotal("딸기"));

        assertEquals(exception.getApiResponseEnum().getMessage(), ApiResponseReportEnum.REPORT_NOT_FOUND.getMessage());

    }
}
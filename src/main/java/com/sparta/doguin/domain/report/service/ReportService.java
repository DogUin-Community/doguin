package com.sparta.doguin.domain.report.service;

import com.sparta.doguin.domain.common.exception.HandleNotFound;
import com.sparta.doguin.domain.common.exception.InvalidRequestException;
import com.sparta.doguin.domain.common.response.ApiResponseReportEnum;
import com.sparta.doguin.domain.report.dto.ReportRequest;
import com.sparta.doguin.domain.report.dto.ReportResponse;
import com.sparta.doguin.domain.report.entity.Report;
import com.sparta.doguin.domain.report.repository.ReportRepository;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.doguin.domain.report.ReportType.*;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserService userService;

    public ReportService(ReportRepository reportRepository, UserService userService) {
        this.reportRepository = reportRepository;
        this.userService = userService;
    }

    /**
     * 신고 등록
     *
     * @param user 신고자
     * @param reportRequest 신고에 관한 내용이 들어있다. (신고 제목, 사유, 신고 대상 id)
     * @since 1.0
     * @throws InvalidRequestException 이미 등록된 신고가 존재할 경우 발생
     * @author 김창민
     */
    @Transactional
    public void report(User user, ReportRequest.Report reportRequest) {
        User reportee = userService.findById(reportRequest.reporteeId());

        if(reportRepository.findByReporterIdAndReporteeId(user.getId(),reportRequest.reporteeId()).isPresent()){
            throw new InvalidRequestException(ApiResponseReportEnum.REPORT_ALREADY_EXIST);
        }
        Report report = new Report(reportRequest.title(), reportRequest.content(), user, reportee, REPORT_NOT_CONFIRMED);
        reportRepository.save(report);
    }

    /**
     * 신고 수락
     *
     * @param reportId 신고 번호
     * @since 1.0
     * @throws HandleNotFound 해당 신고가 존재하지 않을 경우 발생
     * @author 김창민
     */
    @Transactional
    public void accept(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(
                () -> new HandleNotFound(ApiResponseReportEnum.REPORT_NOT_FOUND)
        );
        report.changeReportType(REPORT_ACCEPT);
    }

    /**
     * 신고 거절
     *
     * @param reportId 신고 번호
     * @since 1.0
     * @throws HandleNotFound 해당 신고가 존재하지 않을 경우 발생
     * @author 김창민
     */
    @Transactional
    public void reject(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(
                () -> new HandleNotFound(ApiResponseReportEnum.REPORT_NOT_FOUND)
        );
        report.changeReportType(REPORT_REJECT);
    }

    /**
     * 신고 전체 내역 보기
     *
     * @param user 신고한 유저
     * @param page 페이지 번호
     * @param size 한 페이지당 게시물 개수
     * @since 1.0
     * @author 김창민
     */
    public Page<ReportResponse.ReportView> reportViewAll(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ReportResponse.ReportView> reports = reportRepository.findAllByReporterId(pageable,user.getId());
        return reports;
    }

    /**
     * 내가 신고한 특정 유저 결과 확인
     *
     * @param user 신고한 유저
     * @param reporteeId 신고 당한 유저 id
     * @since 1.0
     * @throws HandleNotFound 해당 신고가 존재하지 않을 경우 발생
     * @author 김창민
     */
    public ReportResponse.ReportView reportSearch(User user, Long reporteeId) {
        Report report = reportRepository.findByReporterIdAndReporteeId(user.getId(),reporteeId).orElseThrow(
                () -> new HandleNotFound(ApiResponseReportEnum.REPORT_NOT_FOUND)
        );

        return new ReportResponse.ReportView(report.getId(),report.getTitle(),report.getReportee().getNickname(),report.getReportType());
    }

    /**
     * admin유저가 보는 특정 유저가 신고 당한 횟수
     *
     * @param reporteeId 신고한 유저
     * @since 1.0
     * @throws HandleNotFound 해당 신고가 존재하지 않을 경우 발생
     * @author 김창민
     */
    public ReportResponse.ReportTotalView reportTotal(Long reporteeId) {

        ReportResponse.ReportTotalView response = reportRepository.findCountByReporteeId(reporteeId).orElseThrow(
                () -> new HandleNotFound(ApiResponseReportEnum.REPORT_NOT_FOUND)
        );

        return response;
    }
}

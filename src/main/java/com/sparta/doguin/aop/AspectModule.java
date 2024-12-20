package com.sparta.doguin.aop;

import com.sparta.doguin.domain.common.exception.BaseException;
import com.sparta.doguin.domain.report.dto.ReportRequest;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.notification.discord.DiscordMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j(topic = "AspectModule")
@Component
@RequiredArgsConstructor
public class AspectModule {
    private final DiscordMessageService dms;

    /**
     * 특정 유저가 어떤 유저를 신고했는지 알람을 보냄
     *
     * @param jp
     */
    @Async
    @Before("execution(public * com.sparta.doguin.domain.report.service.ReportService.report(..))")
    public void before(JoinPoint jp) {
        ReportRequest.Report reqReport = (ReportRequest.Report) getArg(jp,1);
        User reportUser = (User) getArg(jp,0);
        dms.sendMsgReportChannel(reportUser.getId(),reqReport.reporteeNickname(), reqReport.title(),reqReport.content());
    }

    /**
     * 비즈니스 로직에서 에러 발생시 디스코드 알림 전송
     *
     * @param jp
     */
    @AfterThrowing(value = "execution(public * com.sparta.doguin.domain..service..*(..))", throwing = "e")
    public void around(JoinPoint jp, BaseException e) {
        sendErrorNotification(jp,e);
    }

    @Async
    public void sendErrorNotification(JoinPoint jp,BaseException e) {
        String className = jp.getSignature().getDeclaringTypeName();
        String methodName = jp.getSignature().getName();
        dms.sendMsgErrorChannel(
                e.getApiResponseEnum().getMessage(),
                e.getApiResponseEnum().getCode(),
                "0",
                className,
                methodName
        );
    }

    private Object getArg(JoinPoint jp, int order) {
        return jp.getArgs()[order];
    }



}

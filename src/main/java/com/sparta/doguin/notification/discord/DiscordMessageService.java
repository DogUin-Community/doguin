package com.sparta.doguin.notification.discord;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;

@Slf4j(topic = "DiscordMessageService")
@RequiredArgsConstructor
@Service
public class DiscordMessageService {
    @Value("${discord.report_url}")
    private String reportUrl;
    @Value("${discord.error_url}")
    private String errorUrl;

    /**
     * 특정 메세지를 alert 채널에게 전송하는 메서드
     *
     * @param title alert 채널에 띄울 제목
     * @param description alert 채널에 띄울 본문
     */
    public void sendMsgReportChannel(Long report, String reported, String title, String description){
        DiscordWebhook webhook = new DiscordWebhook(reportUrl);
        webhook.addEmbed(
                new DiscordWebhook.EmbedObject()
                        .setTitle("신고 제목 : " + title)
                        .setDescription("신고 내용 : " + description)
                        .setColor(Color.YELLOW)
                        .addField("report", "Id : " + report + " 유저가 신고했습니다", true)
                        .addField("reported", "Id : " + reported + " 유저가 신고당했습니다", true)
                        .setFooter("report","https://png.pngtree.com/png-vector/20190118/ourmid/pngtree-vector-notification-icon-png-image_323839.jpg")


        );
        try {
            webhook.execute();
        } catch (Exception e) {
            log.error("Error {} ", e.getMessage());
        }
    }

    public void sendMsgErrorChannel(String errorMsg, int code, String clientIp, String className, String methodName){
        DiscordWebhook webhook = new DiscordWebhook(errorUrl);
        webhook.addEmbed(
                new DiscordWebhook.EmbedObject()
                        .setTitle("에러 사유 : " + errorMsg)
                        .setDescription("에러 코드 : " + code)
                        .setColor(Color.RED)
                        .setFooter("error","https://w7.pngwing.com/pngs/285/84/png-transparent-computer-icons-error-super-8-film-angle-triangle-computer-icons-thumbnail.png")
                        .addField("요청된 클라이언트 ID", clientIp, false)
                        .addField("문제 발생 클래스",className, true)
                        .addField("문제 발생 메서드", methodName, true)
        );
        try {
            webhook.execute();
        } catch (Exception e) {
            log.error("Error {} ", e.getMessage());
        }
    }

}

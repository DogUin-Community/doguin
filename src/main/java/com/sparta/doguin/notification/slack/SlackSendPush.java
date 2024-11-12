package com.sparta.doguin.notification.slack;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class SlackSendPush {

    @Value(value = "${slack.bot-token}")
    private String token;

    @Async // 비동기
    protected void sendPush(String slackId, String nickName, String message) {
        String url = "https://slack.com/api/chat.postMessage";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json; charset=utf-8");

        // JSON 본문 생성
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("channel", slackId);
        jsonObject.addProperty("text", nickName + " " + message);
        String body = jsonObject.toString();

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

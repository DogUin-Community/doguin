package com.sparta.doguin.notification.slack;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SlackSendPush {

    @Value(value = "${slack.bot-token}")
    private String token;

    private final WebClient webClient;

    public SlackSendPush(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://slack.com/api").build();
    }

    protected Mono<Void> sendPush(String slackId, String nickName, String message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("channel", slackId);
        jsonObject.addProperty("text", nickName + " " + message);
        String body = jsonObject.toString();

        return webClient.post()
                .uri("/chat.postMessage")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Void.class)  // 응답의 바디는 무시하고 비동기 작업만 수행
                .doOnError(e -> log.error("Slack 메시지 전송 실패: ", e));
    }
}

package com.sparta.doguin.notification.slack;

import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.google.gson.JsonObject;

@Component
public class SlackEmailLookupService {

    @Value(value = "${slack.bot-token}")
    private String token;

    private final WebClient webClient;

    public SlackEmailLookupService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://slack.com/api").build();
    }

    public Mono<String> findSlackIdByEmail(String email) {
        String url = "/users.lookupByEmail?email=" + email;

        return webClient.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
                    JsonObject profile = jsonObject.getAsJsonObject("user");
                    return profile.get("id").getAsString();
                })
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.empty(); // 오류 발생 시 빈 Mono 반환
                });
    }


    /*
        protected String findEmail(String email) {
        String url = "https://slack.com/api/users.lookupByEmail?email=" + email;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url, HttpMethod.GET, requestEntity, String.class);

            String response = responseEntity.getBody();
            JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
            JsonObject profile = jsonObject.getAsJsonObject("user");

            String id = profile.get("id").getAsString();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    */

}
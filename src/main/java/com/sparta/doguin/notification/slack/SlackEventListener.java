package com.sparta.doguin.notification.slack;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sparta.doguin.domain.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SlackEventListener {

    private static final Logger log = LoggerFactory.getLogger(SlackEventListener.class);
    @Value(value = "${slack.bot-token}")
    private String token;

    private final SlackSendPush slackSendPush;
    private final FollowService followService;

    @EventListener
    public void scrollFollower(SlackEventClass slackEventClass) throws InterruptedException {

        Long userId = slackEventClass.getUserId();
        String nickName = slackEventClass.getNickName();
        String message = slackEventClass.getMessage();

//        List<FollowResponse.Follow> followerUserEmail = followService.getFollowerList(userId);
//
//        for(FollowResponse.Follow val : followerUserEmail){
//            String userEmail = val.email();
//            String slackId = findEmail(userEmail);
//            sendPush(slackId,nickName,message);
//        }

        List<String> emails = new ArrayList<>();
        emails.add("ksjchm4@gmail.com");
        emails.add("tiyuuuu9@gmail.com");
        emails.add("chukye1216@gmail.com");
        emails.add("dbstj4566@naver.com");
        for (String email  : emails) {
            String userEmail = email;
            String slackId = findEmail(userEmail);
            slackSendPush.sendPush(slackId, nickName, message);
        }



    }


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


}

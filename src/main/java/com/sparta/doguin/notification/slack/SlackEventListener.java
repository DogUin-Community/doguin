package com.sparta.doguin.notification.slack;

import com.sparta.doguin.domain.follow.dto.FollowResponse;
import com.sparta.doguin.domain.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SlackEventListener {

    @Value(value = "${slack.bot-token}")
    private String token;

    private final SlackSendPush slackSendPush;
    private final FollowService followService;
    private final SlackEmailLookupService slackEmailLookupService;


    @EventListener
    public void scrollFollower(SlackEventClass slackEventClass){

        Long userId = slackEventClass.getUserId();
        String nickName = slackEventClass.getNickName();
        String message = slackEventClass.getMessage();

        List<FollowResponse.Follow> followerUserEmail = followService.getFollowerList(userId);


        for (FollowResponse.Follow val : followerUserEmail) {
            String userEmail = val.email();
            slackEmailLookupService.findSlackIdByEmail(userEmail)
                    .flatMap(slackId -> slackSendPush.sendPush(slackId, nickName, message))
                    .subscribe(); // 비동기 호출 시작
        }

    }





}

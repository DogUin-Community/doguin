package com.sparta.doguin.notification.slack;

import lombok.Getter;

@Getter
public class SlackEventClass {

    private Long userId;
    private String nickName;
    private String message;

    public SlackEventClass(Long userId, String nickName, String message) {
        this.userId = userId;
        this.nickName = nickName;
        this.message = message;
    }
}

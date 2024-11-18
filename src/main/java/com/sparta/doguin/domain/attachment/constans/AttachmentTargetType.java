package com.sparta.doguin.domain.attachment.constans;

import lombok.Getter;

@Getter
public enum AttachmentTargetType {
    PORTFOLIO("portfolio"),
    OUTSOURCING("outsourcing"),
    BULLETIN("bulletin"),
    EVENT("event"),
    INQUIRY("inquiry"),
    NOTICE("notice"),
    PROFILE("profile"),
    QUESTION("question"),
    DISCUSSION("discussion"),
    REPLY("reply"),;

    private final String target;

    AttachmentTargetType(String target) {
        this.target = target;
    }

}

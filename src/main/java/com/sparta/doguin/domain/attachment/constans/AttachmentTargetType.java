package com.sparta.doguin.domain.attachment.constans;

import lombok.Getter;

// test
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
    ;

    private final String target;

    AttachmentTargetType(String target) {
        this.target = target;
    }

}

package com.sparta.doguin.domain.attachment.constans;

import lombok.Getter;

@Getter
public enum AttachmentTargetType {
    PORTFOLIO("portfolio"),
    OUTSOURCING("outsourcing"),
    ;

    private final String target;

    AttachmentTargetType(String target) {
        this.target = target;
    }

}

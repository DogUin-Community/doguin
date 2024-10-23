package com.sparta.doguin.domain.matching.constans;

import lombok.Getter;

@Getter
public enum MathingStatusType {
    YES("수락"),
    NO("거절"),
    READY("대기");

    private final String status;

    MathingStatusType(String status) {
        this.status = status;
    }
}

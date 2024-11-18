package com.sparta.doguin.domain.user.enums;

import com.sparta.doguin.domain.common.exception.UserException;
import com.sparta.doguin.domain.common.response.ApiResponseUserEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UserGrade {
    C_NECK("C자목"),
    STRAIGHT_NECK("일자목"),
    TURTLE_NECK("거북목");

    private final String userType;

    public static UserGrade of(String grade) {
        return Arrays.stream(UserGrade.values())
                .filter(r -> r.name().equalsIgnoreCase(grade))
                .findFirst()
                .orElseThrow(() -> new UserException(ApiResponseUserEnum.USER_GRADE_INVALID));
    }
}
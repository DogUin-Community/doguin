package com.sparta.doguin.domain.user.enums;

import com.sparta.doguin.domain.common.exception.UserException;
import com.sparta.doguin.domain.common.response.ApiResponseUserEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UserType {
    INDIVIDUAL("개인"),
    COMPANY("기업");

    private final String userType;

    public static UserType of(String type) {
        return Arrays.stream(UserType.values())
                .filter(r -> r.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new UserException(ApiResponseUserEnum.USER_TYPE_INVALID));
    }
}
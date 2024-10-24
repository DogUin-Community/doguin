package com.sparta.doguin.domain.user.dto;

import com.sparta.doguin.domain.user.enums.UserGrade;
import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;

public sealed interface UserResponse permits UserResponse.User, UserResponse.Check, UserResponse.Update {
    // 유저의 기본값 응답
    record User(Long userId, String email, String nickname) implements UserResponse {
    }

    // 회원 정보 조회 시 응답
    record Check(
            Long id,
            String email,
            String nickname,
            UserType userType,
            UserRole userRole,
            String profileImage,
            String introduce,
            String homeAddress,
            String gitAddress,
            String blogAddress,
            UserGrade userGrade
    ) implements UserResponse {}

    // 회원 정보 수정 시 응답
    record Update(
            Long id,
            String email,
            String nickname,
            UserType userType,
            UserRole userRole,
            String profileImage,
            String introduce,
            String homeAddress,
            String gitAddress,
            String blogAddress
    ) implements UserResponse {}
}
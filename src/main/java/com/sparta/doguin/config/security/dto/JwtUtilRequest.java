package com.sparta.doguin.config.security.dto;

import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;

public sealed interface JwtUtilRequest permits JwtUtilRequest.CreateToken {
    record CreateToken(
            Long userId,
            String email,
            String nickname,
            UserType userType,
            UserRole userRole
    ) implements JwtUtilRequest {}
}
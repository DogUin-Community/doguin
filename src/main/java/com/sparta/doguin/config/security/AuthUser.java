package com.sparta.doguin.config.security;

import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser {
    private final Long userId;
    private final String email;
    private final String nickname;
    private final UserType userType;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(Long userId, String email, String nickname, UserType userType, UserRole role) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.userType = userType;
        this.authorities = List.of(new SimpleGrantedAuthority(role.name()));
    }
}
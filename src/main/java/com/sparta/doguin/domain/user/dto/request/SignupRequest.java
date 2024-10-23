package com.sparta.doguin.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    // 회원가입 필수사항
    private String email;
    private String password;
    private String nickname;
    private String userType;
    private String userRole;

    // 회원가입 선택사항
    private String profileImage;
    private String introduce;
    private String homeAddress;
    private String gitAddress;
    private String blogAddress;
}
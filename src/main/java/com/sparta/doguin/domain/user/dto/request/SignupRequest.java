package com.sparta.doguin.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    // 회원가입 필수사항
    @Email
    @NotBlank
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[\\p{Punct}])[A-Za-z\\d\\p{Punct}]{8,20}$",
            message = "비밀번호는 최소 8자리 이상, 숫자, 영문, 특수문자를 1자 이상 포함되어야 합니다.")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank
    private String nickname;

    @NotBlank
    private String userType;

    @NotBlank
    private String userRole;

    // 회원가입 선택사항
    private String profileImage;
    private String introduce;
    private String homeAddress;
    private String gitAddress;
    private String blogAddress;
}
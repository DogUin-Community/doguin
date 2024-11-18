package com.sparta.doguin.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public sealed interface UserRequest permits UserRequest.Signup, UserRequest.Signin, UserRequest.Update {

    // 회원가입 요청을 위한 Record
    record Signup(
            @Email(message = "이메일 형식이 올바르지 않습니다.")
            @NotBlank(message = "이메일을 입력해주세요.")
            String email,

            @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[\\p{Punct}])[A-Za-z\\d\\p{Punct}]{8,20}$",
                    message = "비밀번호는 최소 8자리 이상, 숫자, 영문, 특수문자를 1자 이상 포함되어야 합니다.")
            @NotBlank(message = "비밀번호를 입력해주세요.")
            String password,

            @NotBlank(message = "닉네임을 입력해주세요.")
            String nickname,

            @NotBlank(message = "유저 타입을 입력해주세요.")
            String userType,

            @NotBlank(message = "유저 역할을 입력해주세요.")
            String userRole,

            // 선택 항목
            String profileImage,
            String introduce,
            String homeAddress,
            String gitAddress,
            String blogAddress
    ) implements UserRequest {}

    // 로그인 요청을 위한 Record
    record Signin(
            @Email(message = "이메일 형식이 올바르지 않습니다.")
            @NotBlank(message = "이메일을 입력해주세요.")
            String email,

            @NotBlank(message = "비밀번호를 입력해주세요.")
            String password
    ) implements UserRequest {}

    // 회원 수정 요청을 위한 Record
    record Update(
            @Email(message = "이메일 형식이 올바르지 않습니다.")
            @NotBlank(message = "이메일을 입력해주세요.")
            String email,

            @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[\\p{Punct}])[A-Za-z\\d\\p{Punct}]{8,20}$",
                    message = "비밀번호는 최소 8자리 이상, 숫자, 영문, 특수문자를 1자 이상 포함되어야 합니다.")
            @NotBlank(message = "비밀번호를 입력해주세요.")
            String password,

            @NotBlank(message = "닉네임을 입력해주세요.")
            String nickname,

            // 선택 항목
            String profileImage,
            String introduce,
            String homeAddress,
            String gitAddress,
            String blogAddress
    ) implements UserRequest {}
}

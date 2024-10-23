package com.sparta.doguin.domain.user.entity;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.user.enums.UserGrade;
import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 회원가입 시 필수 : 이메일
    @Column(nullable = false, unique = true)
    private String email;

    // 회원가입 시 필수 : 비밀번호
    @Column(nullable = false)
    private String password;

    // 회원가입 시 필수 : 닉네임
    @Column(nullable = false)
    private String nickname;

    // 회원가입 시 필수 : 유저유형(개인/기업)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    // 회원가입 시 필수 : 유저권한(유저/관리자)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    // 회원가입 시 선택 : 프로필 이미지
    @Column(nullable = true)
    private String profileImage;

    // 회원가입 시 선택 : 자기소개
    @Column(nullable = true)
    private String introduce;

    // 회원가입 시 선택 : 집 주소
    @Column(nullable = true)
    private String homeAddress;

    // 회원가입 시 선택 : 깃 주소
    @Column(nullable = true)
    private String gitAddress;

    // 회원가입 시 선택 : 블로그 주소
    @Column(nullable = true)
    private String blogAddress;

    @Enumerated(EnumType.STRING)
    private UserGrade userGrade;

    // 필수 정보만 받는 생성자
    public User(Long id, String email, String password, String nickname, UserType userType, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userType = userType;
        this.userRole = userRole;
        this.userGrade = UserGrade.C_NECK; // 회원가입 시 기본값으로 설정
    }

    // 필수 정보 + 선택 정보 포함하는 생성자
    public User(Long id, String email, String password, String nickname, UserType userType, UserRole userRole,
                String profileImage, String introduce, String homeAddress, String gitAddress, String blogAddress) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userType = userType;
        this.userRole = userRole;
        this.profileImage = profileImage;
        this.introduce = introduce;
        this.homeAddress = homeAddress;
        this.gitAddress = gitAddress;
        this.blogAddress = blogAddress;
    }

    // Builder 패턴을 사용하여 유연하게 객체를 생성
    @Builder
    public static User createUser(Long id, String email, String password, String nickname, UserType userType, UserRole userRole,
                                  String profileImage, String introduce, String homeAddress, String gitAddress, String blogAddress) {
        return new User(id, email, password, nickname, userType, userRole, profileImage, introduce, homeAddress, gitAddress, blogAddress);
    }

    // fromAuthUser 메서드 (로그인한 사용자의 인증 정보를 User로 변환)
    public static User fromAuthUser(AuthUser authUser) {
        String roleName = authUser.getAuthorities().iterator().next().getAuthority();
        return User.builder()
                .id(Long.parseLong(authUser.getUserId()))
                .email(authUser.getEmail())
                .userRole(UserRole.of(roleName))
                .build();  // 선택 필드들은 나중에 설정
    }
}

package com.sparta.doguin.domain.user.entity;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User(String email, String password, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    private User(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }

    public static User fromAuthUser(AuthUser authUser) {
        String roleName = authUser.getAuthorities().iterator().next().getAuthority();;
        return new User(Long.parseLong(authUser.getUserId()), authUser.getEmail(), UserRole.of(roleName));
    }
}

package com.sparta.doguin.domain.user.service;

import com.sparta.doguin.common.config.JwtUtil;
import com.sparta.doguin.domain.user.dto.request.SigninRequest;
import com.sparta.doguin.domain.user.dto.request.SignupRequest;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public String signup(SignupRequest signupRequest) {
        User newUser = new User(signupRequest.getEmail(), signupRequest.getPassword(), UserRole.of(signupRequest.getUserRole()));
        User saveduser = userRepository.save(newUser);

        return jwtUtil.createToken(saveduser.getId(), saveduser.getEmail(), saveduser.getUserRole());
    }

    @Transactional(readOnly = true)
    public String signin(SigninRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());
    }
}
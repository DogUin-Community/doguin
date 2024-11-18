package com.sparta.doguin.security;

import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;
import com.sparta.doguin.security.dto.JwtUtilRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(JwtUtilRequest.CreateToken createToken) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(createToken.userId()))
                .claim("email", createToken.email())
                .claim("nickname", createToken.nickname())
                .claim("userType", createToken.userType().getUserType())
                .claim("userRole", createToken.userRole().getUserRole())
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                .setIssuedAt(date) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();
    }

    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(BEARER_PREFIX.length()); // Bearer 접두사 제거
        }
        log.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public AuthUser extractAuthUser(String token) {
        Claims claims = extractClaims(token);
        Long userId = claims.get("sub", Long.class);
        String nickname = claims.get("nickname", String.class);
        String email = claims.get("email", String.class);
        UserRole role = claims.get("userRole", UserRole.class);
        UserType type = claims.get("userType", UserType.class);
        return new AuthUser(userId, email,nickname,type ,role);
    }

    // 토큰을 헤더에 저장하는 메서드
    public void addTokenToResponseHeader(String token, HttpServletResponse response) {
        response.addHeader("Authorization", token);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
    }
}
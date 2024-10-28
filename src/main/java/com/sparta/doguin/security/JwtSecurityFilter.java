package com.sparta.doguin.security;

import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpRequest,
            @NonNull HttpServletResponse httpResponse,
            @NonNull FilterChain chain
    ) throws ServletException, IOException {
        // 요청 헤더에서 "Authorization" 헤더 값을 가져옴
        String authorizationHeader = httpRequest.getHeader("Authorization");

        // Authorization 헤더가 존재하고, Bearer로 시작하는지 확인
        // header에 이미 Bearer 를 자르고 주기 때문에 Bearer 로 시작 할 수 없음 by 김경민
        if (authorizationHeader != null && authorizationHeader.startsWith("ey")) {
            // Bearer 접두사를 제거하고 순수한 JWT만 추출
            // 이기 Bearer 가 잘려져있는 상태이므로, subString을 할 필요가 없음 by 김경민
            String jwt = authorizationHeader;
            try {
                // JWT에서 사용자 정보(Claims)를 추출
                Claims claims = jwtUtil.extractClaims(jwt);
                // Claims에서 각 정보 추출
                Long userId = Long.parseLong(claims.getSubject());
                String email = claims.get("email", String.class);
                String nickname = claims.get("nickname", String.class);
                UserType userType = UserType.of(claims.get("userType", String.class));
                UserRole userRole = UserRole.of(claims.get("userRole", String.class));

                // 사용자 인증이 아직 설정되지 않았다면
                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // AuthUser 객체를 생성
                    AuthUser authUser = new AuthUser(userId, email, nickname, userType, userRole);

                    // JwtAuthenticationToken으로 인증 객체 생성
                    JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));

                    // SecurityContextHolder에 인증 객체 설정 (사용자 인증 처리)
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (SecurityException | MalformedJwtException e) {
                log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않는 JWT 서명입니다.");
            } catch (ExpiredJwtException e) {
                log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
            } catch (UnsupportedJwtException e) {
                log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
            } catch (Exception e) {
                log.error("Internal server error", e);
                httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        // 요청을 다음 필터로 넘김
        chain.doFilter(httpRequest, httpResponse);
    }
}

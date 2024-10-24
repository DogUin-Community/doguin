package com.sparta.doguin.config;

import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.config.security.JwtAuthenticationToken;
import com.sparta.doguin.config.security.JwtUtil;
import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(@NonNull Message<?> message,@NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        String token = accessor.getFirstNativeHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            try {
                String jwt = token.substring(7);  // "Bearer " 제거
                Claims claims = jwtUtil.extractClaims(jwt);

                Long userId = Long.valueOf(claims.getSubject());
                String email = claims.get("email", String.class);
                String nickname = claims.get("nickname", String.class);
                UserType userType = UserType.of(claims.get("userType", String.class));
                UserRole userRole = UserRole.of(claims.get("userRole", String.class));

                AuthUser authUser = new AuthUser(userId, email, nickname, userType, userRole);

                JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                accessor.setUser(authenticationToken);
            } catch (Exception e) {
                log.error("JWT 토큰 인증 실패", e);
                SecurityContextHolder.clearContext();
            }
        }

        return message;
    }
}

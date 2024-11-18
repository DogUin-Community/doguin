package com.sparta.doguin.config;

import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;
import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.security.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String token = accessor.getFirstNativeHeader("Authorization");

        if (token != null && token.startsWith("ey")) {
            try {
                log.info("Token Received: {}", token);
                Claims claims = jwtUtil.extractClaims(token);
                String userId = claims.get("sub", String.class);
                String nickname = claims.get("nickname", String.class);
                String email = claims.get("email", String.class);
                String roleString = claims.get("userRole", String.class);
                String typeString = claims.get("userType", String.class);
                UserRole role = UserRole.valueOf(roleString); // 문자열을 Enum으로 변환
                UserType type = UserType.valueOf(typeString); // 문자열을 Enum으로 변환
                AuthUser authUser = new AuthUser(
                        Long.valueOf(userId),
                        nickname,
                        email,
                        type,
                        role
                );
                accessor.setUser(authUser);
                log.info("JWT Claims: {}", claims);
                log.info("Principal set in WebSocketAuthInterceptor: {}", accessor.getUser());

            } catch (Exception e) {
                log.error("JWT Authentication failed", e);
                try {
                    throw new AccessDeniedException("Invalid JWT token");
                } catch (AccessDeniedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
    }

}


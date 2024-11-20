package com.sparta.doguin.config;

import com.sparta.doguin.domain.chatting.entity.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(pattern);
            String body = new String(message.getBody());
            log.info("Received message: {} from channel: {}", body, channel);

            // 메시지가 배열로 수신될 경우 이를 단일 객체로 변환
            if (body.startsWith("[")) {
                // 배열을 단일 객체로 변환
                body = body.substring(1, body.length() - 1);
            }

            // 메시지를 ChatMessage 객체로 변환
            ChatMessage chatMessage = objectMapper.readValue(body, ChatMessage.class);
            log.info("ChatMessage object: {}", chatMessage);

            // 해당 채팅방의 주제로 메시지 전송
            messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getRoomId(), chatMessage);
        } catch (Exception e) {
            log.error("Error processing message: ", e);
        }
    }
}

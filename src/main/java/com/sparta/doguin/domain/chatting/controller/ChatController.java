package com.sparta.doguin.domain.chatting.controller;

import com.sparta.doguin.domain.chatting.dto.ChatRequest;
import com.sparta.doguin.domain.chatting.dto.ChatResponse;
import com.sparta.doguin.domain.chatting.service.ChatService;
import com.sparta.doguin.security.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat/send")
    public ChatResponse.MessageResponse sendMessage(Principal principal, ChatRequest.MessageSendRequest messageDto) {
        AuthUser authUser = (AuthUser) principal;
        return chatService.processSendMessage(authUser.getUserId(), messageDto);
    }

    @MessageMapping("/chat/create")
    @SendTo("/user/queue/chat/created")
    public ChatResponse.RoomResponse createRoom(Principal principal, Long applicantId) {
        AuthUser authUser = (AuthUser) principal;
        try {
            return chatService.processCreateRoom(authUser, applicantId);
        } catch (Exception e) {
            log.error("Error while creating room:", e);
            throw e; // 필요하면 특정 예외 처리 후 클라이언트로 메시지 전송
        }
    }

    @MessageMapping("/chat/leave")
    public void leaveRoom(Principal principal, String roomId) {
        AuthUser authUser = (AuthUser) principal;
        chatService.leaveRoom(authUser.getUserId(), roomId);
    }

    // New Method: 고유 senderId를 클라이언트에 전송
    @MessageMapping("/chat/senderId")
    public ChatResponse.SenderIdResponse getSenderId(Principal principal) {
        AuthUser authUser = (AuthUser) principal;
        int senderId = chatService.generateSenderId(authUser); // 고유 senderId 생성
        return new ChatResponse.SenderIdResponse(senderId);
    }
}

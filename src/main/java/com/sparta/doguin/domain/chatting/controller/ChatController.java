package com.sparta.doguin.domain.chatting.controller;

import com.sparta.doguin.domain.chatting.dto.ChatRequest;
import com.sparta.doguin.domain.chatting.dto.ChatResponse;
import com.sparta.doguin.domain.chatting.service.ChatService;
import com.sparta.doguin.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

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
    public ChatResponse.RoomResponse createRoom(Principal principal, Long applicantId) {
        AuthUser authUser = (AuthUser) principal;
        return chatService.processCreateRoom(authUser, applicantId);
    }

    @MessageMapping("/chat/leave")
    public void leaveRoom(Principal principal, String roomId) {
        AuthUser authUser = (AuthUser) principal;
        chatService.leaveRoom(authUser.getUserId(), roomId);
    }
}

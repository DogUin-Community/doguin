package com.sparta.doguin.domain.chatting.controller;

import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.chatting.dto.ChatRequest;
import com.sparta.doguin.domain.chatting.dto.ChatResponse;
import com.sparta.doguin.domain.chatting.service.ChatService;
import com.sparta.doguin.domain.common.exception.ChatException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseChatEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatrooms")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // 채팅방 생성
    @PostMapping
    public ResponseEntity<ApiResponse<ChatResponse.ChatRoomResponse>> createChatRoom(@RequestBody ChatRequest.ChatRoomRequest chatRoomRequest) {
        ChatResponse.ChatRoomResponse createdChatRoom = chatService.createChatRoom(chatRoomRequest);
        ApiResponse<ChatResponse.ChatRoomResponse> apiResponse = new ApiResponse<>(ApiResponseChatEnum.CHATROOM_CREATE_SUCCESS, createdChatRoom);
        return ApiResponse.of(apiResponse);
    }

    // 메시지 전송
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@RequestBody ChatRequest.MessageRequest messageRequest) {
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            chatService.sendMessage(messageRequest, authUser);
        } catch (ChatException e) {
            sendErrorMessage(e);
        }
    }

    // 채팅방 입장
    @MessageMapping("/chat.enterRoom")
    public void enterRoom(@RequestBody ChatRequest.MessageRequest messageRequest) {
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            chatService.userEnter(messageRequest.chatRoomId(), authUser);
        } catch (ChatException e) {
            sendErrorMessage(e);
        }
    }

    // 채팅방 퇴장
    @MessageMapping("/chat.leaveRoom")
    public void leaveRoom(@RequestBody ChatRequest.MessageRequest messageRequest) {
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            chatService.userExit(messageRequest.chatRoomId(), authUser);
        } catch (ChatException e) {
            sendErrorMessage(e);
        }
    }

    private void sendErrorMessage(ChatException e) {
        messagingTemplate.convertAndSend("/topic/errors",
                new ChatResponse.ErrorMessage(e.getApiResponseEnum().getMessage()));
    }
}

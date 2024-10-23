package com.sparta.doguin.domain.chatting.controller;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.chatting.dto.ChatRequest;
import com.sparta.doguin.domain.chatting.dto.ChatResponse;
import com.sparta.doguin.domain.chatting.service.ChatService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseChatEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatrooms")
public class ChatController {

    private final ChatService chatService;

    // 채팅방 생성
    @PostMapping
    public ResponseEntity<ApiResponse<ChatResponse.ChatRoomResponse>> createChatRoom(@RequestBody ChatRequest.ChatRoomRequest chatRoomRequest) {
        ChatResponse.ChatRoomResponse createdChatRoom = chatService.createChatRoom(chatRoomRequest);
        ApiResponse<ChatResponse.ChatRoomResponse> apiResponse = new ApiResponse<>(ApiResponseChatEnum.CHATROOM_CREATE_SUCCESS, createdChatRoom);
        return ApiResponse.of(apiResponse);
    }

    // 메시지 전송
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/{chatRoomId}")
    public ResponseEntity<ApiResponse<ChatResponse.MessageResponse>> sendMessage(
            @RequestBody ChatRequest.MessageRequest messageRequest,
            @AuthenticationPrincipal AuthUser authUser) {
        ChatResponse.MessageResponse response = chatService.sendMessage(messageRequest, authUser);
        ApiResponse<ChatResponse.MessageResponse> apiResponse = new ApiResponse<>(ApiResponseChatEnum.MESSAGE_SEND_SUCCESS, response);
        return ApiResponse.of(apiResponse);
    }

    // 채팅방 입장
    @MessageMapping("/chat.enterRoom")
    @SendTo("/topic/{chatRoomId}")
    public ResponseEntity<ApiResponse<Void>> enterRoom(@PathVariable Long chatRoomId, @AuthenticationPrincipal AuthUser authUser) {
        chatService.userEnter(chatRoomId, authUser);
        ApiResponse<Void> apiResponse = new ApiResponse<>(ApiResponseChatEnum.CHATROOM_ENTER_SUCCESS);
        return ApiResponse.of(apiResponse);
    }

    // 채팅방 퇴장
    @PostMapping("/{chatRoomId}/leave")
    public ResponseEntity<ApiResponse<Void>> leaveRoom(@PathVariable Long chatRoomId, @AuthenticationPrincipal AuthUser authUser) {
        chatService.userExit(chatRoomId, authUser);
        ApiResponse<Void> apiResponse = new ApiResponse<>(ApiResponseChatEnum.CHATROOM_LEAVE_SUCCESS);
        return ApiResponse.of(apiResponse);
    }
}

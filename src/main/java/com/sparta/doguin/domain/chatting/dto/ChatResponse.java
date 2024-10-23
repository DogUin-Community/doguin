package com.sparta.doguin.domain.chatting.dto;

public sealed interface ChatResponse permits ChatResponse.ChatRoomResponse, ChatResponse.MessageResponse, ChatResponse.ChatMessageResponse {

    record ChatRoomResponse(Long id, String title) implements ChatResponse {}

    record MessageResponse(Long chatRoomId,
                           String content) implements ChatResponse {}

    record ChatMessageResponse(String email,
                               String content,
                               Long chatRoomId,
                               String timestamp) implements ChatResponse {}
}

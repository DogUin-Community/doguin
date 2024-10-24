package com.sparta.doguin.domain.chatting.dto;

public sealed interface ChatRequest permits ChatRequest.ChatRoomRequest, ChatRequest.MessageRequest, ChatRequest.ChatMessageRequest {

    record ChatRoomRequest(String title) implements ChatRequest {}

    record MessageRequest(Long chatRoomId,
                          String content) implements ChatRequest {}

    record ChatMessageRequest(String email,
                              String content,
                              Long chatRoomId,
                              String timestamp) implements ChatRequest {}
}
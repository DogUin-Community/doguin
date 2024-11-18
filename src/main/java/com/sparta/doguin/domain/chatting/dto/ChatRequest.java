package com.sparta.doguin.domain.chatting.dto;

public sealed interface ChatRequest permits ChatRequest.MessageSendRequest {
    record MessageSendRequest(String roomId, String senderId, String content) implements ChatRequest {
    }
}

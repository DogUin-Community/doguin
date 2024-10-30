package com.sparta.doguin.domain.chatting.dto;

import java.time.LocalDateTime;
import java.util.List;

public sealed interface ChatResponse permits ChatResponse.MessageResponse, ChatResponse.RoomResponse {
    record MessageResponse(String messageId, String roomId, Long senderId, String content, LocalDateTime timestamp) implements ChatResponse {}
    record RoomResponse(String roomId, Long creatorId, Long applicantId, List<MessageResponse> messages) implements ChatResponse {}
}

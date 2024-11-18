package com.sparta.doguin.domain.chatting.dto;

import java.util.List;

public sealed interface ChatResponse permits ChatResponse.MessageResponse, ChatResponse.RoomResponse, ChatResponse.SenderIdResponse {
    record MessageResponse(String messageId, String roomId, Long senderId, String content) implements ChatResponse {}
    record RoomResponse(String roomId, Long creatorId, Long applicantId, List<MessageResponse> messages) implements ChatResponse {}
    record SenderIdResponse(int senderId) implements ChatResponse {}
}

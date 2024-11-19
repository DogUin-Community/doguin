package com.sparta.doguin.domain.chatting.service;

import com.sparta.doguin.domain.chatting.dto.ChatRequest;
import com.sparta.doguin.domain.chatting.dto.ChatResponse;
import com.sparta.doguin.domain.chatting.entity.ChatMessage;
import com.sparta.doguin.domain.chatting.entity.ChatRoom;
import com.sparta.doguin.domain.chatting.repository.ChatMessageRepository;
import com.sparta.doguin.domain.chatting.repository.ChatRoomRepository;
import com.sparta.doguin.domain.common.exception.ChatException;
import com.sparta.doguin.domain.common.response.ApiResponseChatEnum;
import com.sparta.doguin.domain.matching.constans.MathingStatusType;
import com.sparta.doguin.domain.matching.entity.Matching;
import com.sparta.doguin.domain.matching.repository.MatchingRepository;
import com.sparta.doguin.domain.user.enums.UserType;
import com.sparta.doguin.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MatchingRepository matchingRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;


    // 메시지 전송 처리
    public ChatResponse.MessageResponse processSendMessage(Long userId, ChatRequest.MessageSendRequest messageDto) {
        ChatMessage savedMessage = saveMessage(messageDto.roomId(), userId, messageDto.content());

//        redisTemplate.convertAndSend("/topic/chat/" + messageDto.roomId(), savedMessage);

        messagingTemplate.convertAndSend("/topic/chat/" + messageDto.roomId(), savedMessage);

        return new ChatResponse.MessageResponse(
                savedMessage.getId(), savedMessage.getRoomId(), userId, savedMessage.getContent()
        );
    }

    // 메시지 저장 메서드
    public ChatMessage saveMessage(String roomId, Long senderId, String content) {
        ChatMessage message = new ChatMessage(roomId, senderId, content, LocalDateTime.now());
        return chatMessageRepository.save(message);
    }

    // 채팅방 나가기 처리
    public void leaveRoom(Long userId, String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ApiResponseChatEnum.CHATROOM_NOT_FOUND));

        if (chatRoom.removeUser(userId) && chatRoom.isEmpty()) {
            deleteRoom(roomId);
        }
    }

    // 채팅방 삭제
    public void deleteRoom(String roomId) {
        chatRoomRepository.deleteByRoomId(roomId);
        chatMessageRepository.deleteAll(chatMessageRepository.findByRoomId(roomId));
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredRooms() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMonths(6);
        chatRoomRepository.deleteByCreatedAtBefore(expirationTime);
    }

    // 채팅방 생성 처리
    public ChatResponse.RoomResponse processCreateRoom(AuthUser authUser, Long applicantId) {
        if (authUser.getUserType() != UserType.COMPANY) {
            throw new ChatException(ApiResponseChatEnum.INVALID_USER_TYPE);
        }

        Long userId = authUser.getUserId();
        Matching matching = matchingRepository.findByUserIdAndOutsourcingUserId(applicantId, userId)
                .orElseThrow(() -> new ChatException(ApiResponseChatEnum.INVALID_APPLICATION_STATUS));

        // 지원 상태 검증
        if (matching.getStatus() != MathingStatusType.READY && matching.getStatus() != MathingStatusType.YES) {
            throw new ChatException(ApiResponseChatEnum.INVALID_APPLICATION_STATUS);
        }

        // 기존 채팅방 중복 생성 방지
        ChatRoom chatRoom = chatRoomRepository.findByCreatorIdAndApplicantId(userId, applicantId)
                .orElseGet(() -> {
                    ChatRoom room = new ChatRoom();
                    room.setRoomId(userId + "_" + applicantId);
                    room.setCreatorId(userId);
                    room.setApplicantId(applicantId);
                    room.setCreatedAt(LocalDateTime.now());
                    return chatRoomRepository.save(room);
                });

        return new ChatResponse.RoomResponse(
                chatRoom.getRoomId(), chatRoom.getCreatorId(), chatRoom.getApplicantId(), List.of()
        );
    }

    public int generateSenderId(AuthUser authUser) {
        // 예: AuthUser의 ID를 기반으로 senderId 생성 (고유값 생성)
        int senderId = authUser.getUserId().hashCode(); // 또는 authUser.getUserId() 사용

        // 클라이언트가 자신에게만 해당하는지 확인할 수 있도록 추가 정보 포함
        ChatResponse.SenderIdResponse response = new ChatResponse.SenderIdResponse(senderId);

        messagingTemplate.convertAndSend("/topic/senderId", response);
        return senderId;
    }


    // 유저가 방에 있는지 검증
    private ChatRoom validateUserInRoom(Long userId, String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ChatException(ApiResponseChatEnum.CHATROOM_NOT_FOUND));

        return chatRoom;
    }
}

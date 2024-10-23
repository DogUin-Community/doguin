package com.sparta.doguin.domain.chatting.service;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.chatting.dto.ChatRequest;
import com.sparta.doguin.domain.chatting.dto.ChatResponse;
import com.sparta.doguin.domain.chatting.entity.ChatRoom;
import com.sparta.doguin.domain.chatting.entity.Message;
import com.sparta.doguin.domain.chatting.entity.UserChatRoom;
import com.sparta.doguin.domain.chatting.repository.ChatRoomRepository;
import com.sparta.doguin.domain.chatting.repository.MessageRepository;
import com.sparta.doguin.domain.chatting.repository.UserChatRoomRepository;
import com.sparta.doguin.domain.common.exception.ChatException;
import com.sparta.doguin.domain.common.response.ApiResponseChatEnum;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private static final int MAX_CHAT_ROOM_CAPACITY = 1000;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final AuthService authService;
    private final UserChatRoomRepository userChatRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;



    // 채팅방 생성
    public ChatResponse.ChatRoomResponse createChatRoom(ChatRequest.ChatRoomRequest chatRoomRequest) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setTitle(chatRoomRequest.title());
        chatRoomRepository.save(chatRoom);

        return new ChatResponse.ChatRoomResponse(chatRoom.getId(), chatRoom.getTitle());
    }

    // 메시지 저장 및 전송
    public ChatResponse.MessageResponse sendMessage(ChatRequest.MessageRequest messageRequest, AuthUser authUser) {
        ChatRoom chatRoom = findChatRoomById(messageRequest.chatRoomId());

        // 채팅방 인원 제한 체크
        checkRoomCapacity(messageRequest.chatRoomId());

        User user = authService.findById(authUser.getUserId());

        // 메시지 생성 및 저장
        Message message = createAndSaveMessage(chatRoom, user, messageRequest.content());

        // WebSocket을 통한 메시지 전송 및 Redis 저장
        sendMessageToTopic(messageRequest.chatRoomId(), authUser.getNickname(), messageRequest.content(), message.getCreatedAt());
        saveMessageToRedis(messageRequest.chatRoomId(), message);

        return new ChatResponse.MessageResponse(messageRequest.chatRoomId(), messageRequest.content());
    }

    // 입장 메시지 처리
    public void userEnter(Long chatRoomId, AuthUser authUser) {
        sendSystemMessage(chatRoomId, authUser.getNickname() + "님이 입장하셨습니다.");
    }

    // 퇴장 메시지 처리 및 방 삭제 여부 확인
    public void userExit(Long chatRoomId, AuthUser authUser) {
        sendSystemMessage(chatRoomId, authUser.getNickname() + "님이 퇴장하셨습니다.");

        // 퇴장 후 채팅방 인원 체크 및 삭제 처리
        List<UserChatRoom> userChatRooms = userChatRoomRepository.findByChatRoom_Id(chatRoomId);
        if (userChatRooms.isEmpty()) {
            redisTemplate.delete("chatroom:" + chatRoomId);
            chatRoomRepository.deleteById(chatRoomId);
        }
    }

    // 채팅방 인원 체크
    @Transactional(readOnly = true)
    public boolean isRoomFull(Long chatRoomId) {
        List<UserChatRoom> userChatRooms = userChatRoomRepository.findByChatRoom_Id(chatRoomId);
        return userChatRooms.size() >= MAX_CHAT_ROOM_CAPACITY;
    }

    private ChatRoom findChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatException(ApiResponseChatEnum.CHATROOM_NOT_FOUND));
    }

    private void checkRoomCapacity(Long chatRoomId) {
        if (isRoomFull(chatRoomId)) {
            throw new ChatException(ApiResponseChatEnum.CHATROOM_FULL);
        }
    }

    private Message createAndSaveMessage(ChatRoom chatRoom, User user, String content) {
        Message message = new Message(chatRoom, user, content);
        return messageRepository.save(message);
    }

    private void sendMessageToTopic(Long chatRoomId, String nickname, String content, LocalDateTime createdAt) {
        ChatRequest.ChatMessageRequest chatMessage = new ChatRequest.ChatMessageRequest(
                nickname, content, chatRoomId, createdAt.format(formatter)
        );
        messagingTemplate.convertAndSend("/topic/" + chatRoomId, chatMessage);
    }

    private void saveMessageToRedis(Long chatRoomId, Message message) {
        redisTemplate.opsForList().rightPush("chatroom:" + chatRoomId, message);
    }

    private void sendSystemMessage(Long chatRoomId, String messageContent) {
        ChatRequest.ChatMessageRequest systemMessage = new ChatRequest.ChatMessageRequest(
                "System", messageContent, chatRoomId, LocalDateTime.now().format(formatter) // 일관된 포맷 사용
        );
        messagingTemplate.convertAndSend("/topic/" + chatRoomId, systemMessage);
    }
}

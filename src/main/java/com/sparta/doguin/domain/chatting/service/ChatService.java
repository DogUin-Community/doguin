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
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

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
        ChatRoom chatRoom = chatRoomRepository.findById(messageRequest.chatRoomId())
                .orElseThrow(() -> new ChatException(ApiResponseChatEnum.CHATROOM_NOT_FOUND));

        // 채팅방 인원 제한 체크
        if (isRoomFull(messageRequest.chatRoomId())) {
            throw new ChatException(ApiResponseChatEnum.CHATROOM_FULL);
        }

        // AuthUser에서 userId를 이용해 실제 User 객체를 가져옴
        User user = authService.findById(authUser.getUserId());

        try {
            // 메시지 생성 및 저장
            Message message = new Message(chatRoom, user, messageRequest.content());
            messageRepository.save(message);

            // WebSocket으로 구조화된 DTO 전송
            ChatRequest.ChatMessageRequest chatMessage = new ChatRequest.ChatMessageRequest(
                    authUser.getNickname(),
                    messageRequest.content(),
                    messageRequest.chatRoomId(),
                    message.getCreatedAt().toString()
            );
            messagingTemplate.convertAndSend("/topic/" + messageRequest.chatRoomId(), chatMessage);

            // 메시지를 Redis에 저장
            redisTemplate.opsForList().rightPush("chatroom:" + messageRequest.chatRoomId(), message);

            return new ChatResponse.MessageResponse(messageRequest.chatRoomId(), messageRequest.content());
        } catch (Exception e) {
            throw new ChatException(ApiResponseChatEnum.MESSAGE_SEND_FAILURE);
        }
    }

    // 입장 메시지 처리
    public void userEnter(Long chatRoomId, AuthUser authUser) {
        String nickname = authUser.getNickname();
        ChatRequest.ChatMessageRequest enterMessage = new ChatRequest.ChatMessageRequest(
                nickname,  // 닉네임 사용
                nickname + "님이 입장하셨습니다.",
                chatRoomId,
                LocalDateTime.now().toString()
        );
        messagingTemplate.convertAndSend("/topic/" + chatRoomId, enterMessage);
    }

    // 퇴장 메시지 처리 및 방 삭제 여부 확인
    public void userExit(Long chatRoomId, AuthUser authUser) {
        String nickname = authUser.getNickname();
        ChatRequest.ChatMessageRequest exitMessage = new ChatRequest.ChatMessageRequest(
                nickname,
                nickname + "님이 퇴장하셨습니다.",
                chatRoomId,
                LocalDateTime.now().toString()
        );
        messagingTemplate.convertAndSend("/topic/" + chatRoomId, exitMessage);

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
        return userChatRooms.size() >= 1000;
    }
}

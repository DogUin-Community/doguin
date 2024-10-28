package com.sparta.doguin.domain.chatting.service;

import com.sparta.doguin.security.AuthUser;
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
import com.sparta.doguin.domain.user.service.UserService;
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
    private final UserService userService;
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

    // 메시지 전송
    public void sendMessage(ChatRequest.MessageRequest messageRequest, AuthUser authUser) {
        User user = getUserFromAuth(authUser);
        ChatRoom chatRoom = findChatRoomById(messageRequest.chatRoomId());

        Message message = createAndSaveMessage(chatRoom, user, messageRequest.content());
        sendMessageToTopic(messageRequest.chatRoomId(), user.getNickname(), messageRequest.content(), message.getCreatedAt());
    }

    // 유저 입장 처리
    public void userEnter(Long chatRoomId, AuthUser authUser) {
        checkRoomCapacity(chatRoomId); // 인원 제한 체크
        User user = getUserFromAuth(authUser);
        sendSystemMessage(chatRoomId, user.getNickname() + "님이 입장하셨습니다.");
    }

    // 유저 퇴장 처리 및 방 삭제 여부 확인
    public void userExit(Long chatRoomId, AuthUser authUser) {
        User user = getUserFromAuth(authUser);
        sendSystemMessage(chatRoomId, user.getNickname() + "님이 퇴장하셨습니다.");
        checkAndDeleteEmptyChatRoom(chatRoomId);
    }

    // 유저 정보 가져오기
    private User getUserFromAuth(AuthUser authUser) {
        return userService.findById(authUser.getUserId());
    }

    // 채팅방 찾기
    private ChatRoom findChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatException(ApiResponseChatEnum.CHATROOM_NOT_FOUND));
    }

    // 채팅방 인원 제한 체크
    private void checkRoomCapacity(Long chatRoomId) {
        if (isRoomFull(chatRoomId)) {
            throw new ChatException(ApiResponseChatEnum.CHATROOM_FULL);
        }
    }

    @Transactional(readOnly = true)
    public boolean isRoomFull(Long chatRoomId) {
        List<UserChatRoom> userChatRooms = userChatRoomRepository.findByChatRoom_Id(chatRoomId);
        return userChatRooms.size() >= MAX_CHAT_ROOM_CAPACITY;
    }

    // 채팅방이 비어 있으면 삭제
    private void checkAndDeleteEmptyChatRoom(Long chatRoomId) {
        List<UserChatRoom> userChatRooms = userChatRoomRepository.findByChatRoom_Id(chatRoomId);
        if (userChatRooms.isEmpty()) {
            redisTemplate.delete("chatroom:" + chatRoomId);
            chatRoomRepository.deleteById(chatRoomId);
        }
    }

    // 메시지 생성 및 저장
    private Message createAndSaveMessage(ChatRoom chatRoom, User user, String content) {
        Message message = new Message(chatRoom, user, content);
        return messageRepository.save(message);
    }

    // 메시지 전송
    private void sendMessageToTopic(Long chatRoomId, String sender, String content, LocalDateTime createdAt) {
        ChatRequest.ChatMessageRequest chatMessage = new ChatRequest.ChatMessageRequest(
                sender, content, chatRoomId, createdAt.format(formatter)
        );
        messagingTemplate.convertAndSend("/topic/" + chatRoomId, chatMessage);
    }

    // 시스템 메시지 전송
    private void sendSystemMessage(Long chatRoomId, String messageContent) {
        sendMessageToTopic(chatRoomId, "System", messageContent, LocalDateTime.now());
    }
}

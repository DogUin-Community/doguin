package com.sparta.doguin.domain.chatting.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chatRooms")
public class ChatRoom {
    @Id
    @Indexed(unique = true) // 고유 제약 조건 추가
    private String roomId;

    private Long creatorId;         // 채팅방 생성한 기업 사용자 ID
    private Long applicantId;       // 채팅방에 참여하는 지원자 ID

    private LocalDateTime createdAt;
    private Set<Long> activeUsers = new HashSet<>();

    public boolean removeUser(Long userId) {
        return activeUsers.remove(userId);
    }

    public boolean isEmpty() {
        return activeUsers.isEmpty();
    }
}

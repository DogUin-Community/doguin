package com.sparta.doguin.domain.chatting.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private String roomId;

    private Long creatorId;
    private Long applicantId;
    private Long outsourcingId;

    private LocalDateTime createdAt;
    private Set<Long> activeUsers = new HashSet<>();

    public boolean removeUser(Long userId) {
        return activeUsers.remove(userId);
    }

    public boolean isEmpty() {
        return activeUsers.isEmpty();
    }
}

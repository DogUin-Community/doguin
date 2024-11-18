package com.sparta.doguin.domain.chatting.repository;

import com.sparta.doguin.domain.chatting.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Optional<ChatRoom> findByCreatorIdAndApplicantId(Long creatorId, Long applicantId);
    void deleteByRoomId(String roomId);
    void deleteByCreatedAtBefore(LocalDateTime expirationTime);

    Optional<ChatRoom> findByRoomId(String roomId);

}

package com.sparta.doguin.domain.chatting.repository;


import com.sparta.doguin.domain.chatting.entity.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {
    List<UserChatRoom> findByChatRoom_Id(Long chatRoomId);

}

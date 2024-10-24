package com.sparta.doguin.domain.chatting.repository;

import com.sparta.doguin.domain.chatting.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}

package com.sparta.doguin.domain.discussions.repository;

import com.sparta.doguin.domain.discussions.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}

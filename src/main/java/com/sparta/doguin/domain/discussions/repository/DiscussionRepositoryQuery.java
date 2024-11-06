package com.sparta.doguin.domain.discussions.repository;

import com.sparta.doguin.domain.discussions.entity.Discussion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiscussionRepositoryQuery {
    Page<Discussion> search(String title, String content, String nickname, Pageable pageable);
}

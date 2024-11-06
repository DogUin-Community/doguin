package com.sparta.doguin.domain.discussions.repository;

import com.sparta.doguin.domain.common.exception.DiscussionException;
import com.sparta.doguin.domain.common.response.ApiResponseDiscussionEnum;
import com.sparta.doguin.domain.discussions.entity.Discussion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscussionRepository extends JpaRepository<Discussion, Long>, DiscussionRepositoryQuery {
    @EntityGraph(attributePaths = {"replies"})
    default Discussion findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new DiscussionException(ApiResponseDiscussionEnum.DISCUSSION_NOT_FOUND));
    }
}

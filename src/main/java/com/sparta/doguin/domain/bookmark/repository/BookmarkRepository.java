package com.sparta.doguin.domain.bookmark.repository;

import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import com.sparta.doguin.domain.bookmark.entity.Bookmark;
import com.sparta.doguin.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Page<Bookmark> findBookmarkByUserAndTarget(User user, Pageable pageable, BookmarkTargetType type);
    Page<Bookmark> findBookmarkByUser(User user, Pageable pageable);

    @Query("SELECT b FROM Bookmark b" +
            " WHERE b.targetId = :targetId" +
            " AND b.target = :targetType"
    )
    Optional<Bookmark> findBookmarkByTargetIdAndBookmarkTargetType(Long targetId, BookmarkTargetType targetType);
}

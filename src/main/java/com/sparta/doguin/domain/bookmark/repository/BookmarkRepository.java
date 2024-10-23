package com.sparta.doguin.domain.bookmark.repository;

import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import com.sparta.doguin.domain.bookmark.entity.Bookmark;
import com.sparta.doguin.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Page<Bookmark> findBookmarkByUserAndTarget(User user, Pageable pageable, BookmarkTargetType type);
    Page<Bookmark> findBookmarkByUser(User user, Pageable pageable);
}

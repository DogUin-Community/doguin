package com.sparta.doguin.domain.bookmark.repository;

import com.sparta.doguin.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}

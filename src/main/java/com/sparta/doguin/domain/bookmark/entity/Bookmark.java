package com.sparta.doguin.domain.bookmark.entity;

import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import jakarta.persistence.*;

@Entity
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmark_id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

    private Long targetId;

    @Enumerated(value = EnumType.STRING)
    private BookmarkTargetType target;
}

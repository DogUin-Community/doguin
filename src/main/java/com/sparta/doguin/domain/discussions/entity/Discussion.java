package com.sparta.doguin.domain.discussions.entity;

import com.sparta.doguin.domain.common.Timestamped;
import com.sparta.doguin.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
public class Discussion extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private int viewCount = 0;
    private boolean closed = false;

    @ElementCollection
    private Set<Long> viewedUserIds = new HashSet<>();

    @OneToMany(mappedBy = "discussion",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Discussion(String title,String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
    public String getNickname() {
        return user.getNickname();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void incrementViewCount(Long userId) {
        if (viewedUserIds.add(userId)) {
            this.viewCount++;
        }
    }

    public void checkAndCloseDiscussion() {
        if (!closed && getCreatedAt().plusMonths(3).isBefore(LocalDateTime.now())) {
            this.closed = true;
        }
    }
}

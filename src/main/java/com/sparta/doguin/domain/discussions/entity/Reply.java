package com.sparta.doguin.domain.discussions.entity;

import com.sparta.doguin.domain.common.Timestamped;
import com.sparta.doguin.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Reply extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discussion_id", nullable = false)
    private Discussion discussion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Reply(String content, Discussion discussion, User user) {
        this.content = content;
        this.discussion = discussion;
        this.user = user;
    }

    public String getNickname() {
        return user.getNickname();
    }

    public void updateContent(String content) {
        this.content = content;
    }
}

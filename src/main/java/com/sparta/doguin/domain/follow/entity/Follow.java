package com.sparta.doguin.domain.follow.entity;

import com.sparta.doguin.domain.common.Timestamped;
import com.sparta.doguin.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Follow extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 팔로우한 사용자
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private User followed;

    public Follow(User follower, User followed) {
        this.follower = follower;
        this.followed = followed;
    }
}

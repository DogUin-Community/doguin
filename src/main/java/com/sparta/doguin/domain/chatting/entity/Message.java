package com.sparta.doguin.domain.chatting.entity;

import com.sparta.doguin.domain.common.Timestamped;
import com.sparta.doguin.domain.user.entity.User;
import jakarta.persistence.*;
        import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "message")
public class Message extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "chatRoom_Id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "user_Id")
    private User user;

    @Column(nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Message(ChatRoom chatRoom, User user, String content) {
        this.chatRoom = chatRoom;
        this.user = user;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}

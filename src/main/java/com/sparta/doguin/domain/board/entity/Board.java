package com.sparta.doguin.domain.board.entity;

import com.sparta.doguin.domain.board.BoardType;
import com.sparta.doguin.domain.common.Timestamped;
import com.sparta.doguin.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private BoardType boardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long view;

    public Board(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Board(String title, String content, BoardType boardType, User user) {
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.user = user;
        this.view=0L;
    }

    public void update(String title, String content){
        if(!title.isEmpty()){
            this.title=title;
        }
        if(!content.isEmpty()){
            this.content = content;
        }
    }

    public void changeTotalViewCount(Long now , Long today){
        this.view = now + today;
    }


}

package com.sparta.doguin.domain.board.entity;

import com.sparta.doguin.domain.board.BoardType;
import com.sparta.doguin.domain.common.Timestamped;
import jakarta.persistence.*;

@MappedSuperclass
public abstract class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected String title;
    protected String content;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    protected BoardType boardType;
}

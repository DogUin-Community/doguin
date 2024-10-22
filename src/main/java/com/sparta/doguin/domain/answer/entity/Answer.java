package com.sparta.doguin.domain.answer.entity;

import com.sparta.doguin.domain.answer.enums.CommentType;
import com.sparta.doguin.domain.question.entity.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Answer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private CommentType commentType;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}

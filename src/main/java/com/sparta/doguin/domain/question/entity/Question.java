package com.sparta.doguin.domain.question.entity;

import com.sparta.doguin.domain.answer.entity.Answer;
import com.sparta.doguin.domain.question.dto.QuestionRequest;
import com.sparta.doguin.domain.question.enums.FirstCategory;
import com.sparta.doguin.domain.question.enums.LastCategory;
import com.sparta.doguin.domain.question.enums.QuestionStatus;
import com.sparta.doguin.domain.question.enums.SecondCategory;
import com.sparta.doguin.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Question {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private FirstCategory firstCategory;

    @Enumerated(EnumType.STRING)
    private SecondCategory secondCategory;

    @Enumerated(EnumType.STRING)
    private LastCategory lastCategory;

    @Enumerated(EnumType.STRING)
    private QuestionStatus questionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answerList = new ArrayList<>();

//    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
//    private List<Bookmark> bookmarkList = new ArrayList<>();

    public Question(Long id, String title, String content, FirstCategory firstCategory, SecondCategory secondCategory, LastCategory lastCategory) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.firstCategory = firstCategory;
        this.secondCategory = secondCategory;
        this.lastCategory = lastCategory;
    }

    public Question(String title, String content, FirstCategory firstCategory, SecondCategory secondCategory, LastCategory lastCategory) {
        this.title = title;
        this.content = content;
        this.firstCategory = firstCategory;
        this.secondCategory = secondCategory;
        this.lastCategory = lastCategory;
    }

    public void update(QuestionRequest.UpdateQuestion request) {
        this.title = request.title();
        this.content = request.content();
        this.firstCategory = request.firstCategory();
        this.secondCategory = request.secondCategory();
        this.lastCategory = request.lastCategory();
    }
}

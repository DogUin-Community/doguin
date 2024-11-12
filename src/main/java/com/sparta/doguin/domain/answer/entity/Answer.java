package com.sparta.doguin.domain.answer.entity;

import com.sparta.doguin.domain.answer.dto.AnswerRequest;
import com.sparta.doguin.domain.answer.enums.AnswerStatus;
import com.sparta.doguin.domain.answer.enums.AnswerType;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.common.Timestamped;
import com.sparta.doguin.domain.question.entity.Question;
import com.sparta.doguin.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Answer extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Answer parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private AnswerType answerType;

    private int depth = 0; // 0: 댓글, 1: 대댓글

    @Enumerated(EnumType.STRING)
    private AnswerStatus status = AnswerStatus.ACTIVE; // 기본 상태 ACTIVE

    // 일반 게시글 댓글 생성자
    public Answer(String content, User user, Board board) {
        this.content = content;
        this.user = user;
        this.board = board;
        this.depth = 0;
        this.parent = null;
    }

    // 질문의 답변 생성자
    public Answer(String content, User user, Question question, AnswerType answerType) {
        this.content = content;
        this.user = user;
        this.question = question;
        this.answerType = answerType;
        this.depth = 0;
        this.parent = null;
    }

    // 질문의 답변의 대답변 생성자
    public Answer(String content, User user, Question question, Answer parent, AnswerType answerType) {
        this.content = content;
        this.user = user;
        this.question = question;
        this.answerType = answerType;
        this.parent = parent; // 부모 댓글 설정
        this.depth = 1; // 대댓글 depth는 2
    }

    // 수정
    public void update(AnswerRequest.Request request) {
        this.content = request.content();
    }

    public Long getQuestionId() {
        return question != null ? question.getId() : null;
    }

    // 상태 변경
    public void markAsDeleted() {
        this.status = AnswerStatus.DELETED;
    }

}

package com.sparta.doguin.domain.answer;

public enum AnswerType {
    BULLETIN_ANSWER("Bulletin Answer"),
    NOTICE_ANSWER("Notice Answer"),
    QUESTION_ANSWER("Question Answer"),
    APPLY_ANSWER("Apply Answer"),
    INQUIRY_ANSWER("Inquiry Answer"),;

    private final String type;

    AnswerType(String type) { this.type = type;}
}

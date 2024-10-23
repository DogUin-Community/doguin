package com.sparta.doguin.domain.bookmark.constans;

import lombok.Getter;

@Getter
public enum BookmarkTargetType {
    OUTSOURCING("외주"),
    QUESTION("질문");

    private String type;

    BookmarkTargetType(String type) {
        this.type = type;
    }
}

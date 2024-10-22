package com.sparta.doguin.domain.board.entity;

import com.sparta.doguin.domain.board.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Inquiry extends Board{

    public Inquiry(String title, String content) {
        super();
        this.title = title;
        this.content = content;
        this.boardType = BoardType.BOARD_INQUIRY;
    }
}

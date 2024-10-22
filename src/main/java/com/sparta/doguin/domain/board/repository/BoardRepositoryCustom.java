package com.sparta.doguin.domain.board.repository;

import com.sparta.doguin.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<Board> findAllByTitle(Pageable pageable, String title);
}

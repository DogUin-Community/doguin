package com.sparta.doguin.domain.board.repository;

import com.sparta.doguin.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> {
}

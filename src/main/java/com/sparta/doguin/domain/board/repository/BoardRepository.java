package com.sparta.doguin.domain.board.repository;

import com.sparta.doguin.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board,Long>, BoardRepositoryCustom {


    Optional<Board> findByUserId(Long userId);
}

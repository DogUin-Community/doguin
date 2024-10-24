package com.sparta.doguin.domain.board.repository;

import com.sparta.doguin.domain.board.BoardType;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {


    Page<Board> findAllByBoardType(Pageable pageable, BoardType boardType);

    Page<Board> findAllByTitleAndBoardType(Pageable pageable, String title, BoardType boardType);

    Page<Board> findAllByBoardTypeAndUserId(Pageable pageable, BoardType boardType, Long userId);

    Page<Board> findAllByTitleAndBoardTypeAndUser(Pageable pageable, String title, BoardType boardType, User user);
}

package com.sparta.doguin.domain.board.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.doguin.domain.board.BoardType;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.board.entity.QBoard;
import com.sparta.doguin.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private JPAQueryFactory jpaQueryFactory;
    QBoard board = QBoard.board;

    public BoardRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    @Override
    public Page<Board> findAllByBoardType(Pageable pageable, BoardType boardType) {
        List<Board> result = jpaQueryFactory
                .select(Projections.constructor(Board.class,
                        board.id,
                        board.title,
                        board.content
                ))
                .distinct()
                .from(board)
                .where(
                        eqType(boardType)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(Wildcard.count)
                .from(board)
                .fetchOne();

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public Page<Board> findAllByTitleAndBoardType(Pageable pageable, String title, BoardType boardType) {
        List<Board> result = jpaQueryFactory
                .select(Projections.constructor(Board.class,
                        board.id,
                        board.title,
                        board.content
                ))
                .distinct()
                .from(board)
                .where(
                        eqTitle(title),
                        eqType(boardType)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(Wildcard.count)
                .from(board)
                .fetchOne();

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public Page<Board> findAllByBoardTypeAndUserId(Pageable pageable, BoardType boardType, Long userId) {
        List<Board> result = jpaQueryFactory
                .select(Projections.constructor(Board.class,
                        board.id,
                        board.title,
                        board.content
                ))
                .distinct()
                .from(board)
                .where(
                        eqType(boardType),
                        eqUserId(userId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(Wildcard.count)
                .from(board)
                .fetchOne();

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public Page<Board> findAllByTitleAndBoardTypeAndUser(Pageable pageable, String title, BoardType boardType, User user) {
        List<Board> result = jpaQueryFactory
                .select(Projections.constructor(Board.class,
                        board.id,
                        board.title,
                        board.content
                ))
                .distinct()
                .from(board)
                .where(
                        eqTitle(title),
                        eqType(boardType),
                        eqUserId(user.getId())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(Wildcard.count)
                .from(board)
                .fetchOne();

        return new PageImpl<>(result, pageable, count);
    }

    private BooleanExpression eqTitle(String title){
        return (title!=null && !title.isEmpty()) ? board.title.containsIgnoreCase(title) : null;
    }

    private BooleanExpression eqType(BoardType boardType) {
        return board.boardType.eq(boardType);
    }
    private BooleanExpression eqUserId(Long userId) { return board.user.id.eq(userId); }

}

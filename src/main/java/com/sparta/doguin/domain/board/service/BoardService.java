package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.board.dto.request.BoardRequest;
import com.sparta.doguin.domain.board.dto.response.BoardResponse;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.user.entity.User;
import org.springframework.data.domain.Page;

public interface BoardService {

    // 게시글 생성
    Board create(User user, BoardRequest boardRequest);

    // 게시글 수정
    Board update(User user, Long boardId, BoardRequest boardRequest);


    default Board viewOne(Long boardId) {
        throw new UnsupportedOperationException("viewOne operation not supported");
    }


    default Page<BoardResponse> viewAll(int page, int size) {
        throw new UnsupportedOperationException("viewAll operation not supported");
    }


    default Page<BoardResponse> search(int page, int size, String title) {
        throw new UnsupportedOperationException("search operation not supported");
    }

    default Board viewOneWithUser(Long boardId,User user) {
        throw new UnsupportedOperationException("viewOne operation not supported");
    }


    default Page<BoardResponse> viewAllWithUser(int page, int size,User user) {
        throw new UnsupportedOperationException("viewAll operation not supported");
    }


    default Page<BoardResponse> searchWithUser(int page, int size, String title,User user) {
        throw new UnsupportedOperationException("search operation not supported");
    }



    // 게시글 삭제
    void delete(User user, Long boardId);
}


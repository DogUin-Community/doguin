package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.board.dto.BoardRequest.BoardCommonRequest;
import com.sparta.doguin.domain.board.dto.BoardResponse;
import com.sparta.doguin.domain.board.dto.BoardResponse.BoardCommonResponse;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.user.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BoardService {

    // 게시글 생성
    Board create(User user, BoardCommonRequest boardRequest);

    // 게시글 수정
    Board update(User user, Long boardId, BoardCommonRequest boardRequest);


    default BoardResponse.BoardWithAnswer viewOne(Long boardId) {
        throw new UnsupportedOperationException("viewOne operation not supported");
    }


    default Page<BoardCommonResponse> viewAll(int page, int size) {
        throw new UnsupportedOperationException("viewAll operation not supported");
    }


    default Page<BoardCommonResponse> search(int page, int size, String title) {
        throw new UnsupportedOperationException("search operation not supported");
    }

    default BoardResponse.BoardWithAnswer viewOneWithUser(Long boardId,User user) {
        throw new UnsupportedOperationException("viewOne operation not supported");
    }


    default Page<BoardCommonResponse> viewAllWithUser(int page, int size,User user) {
        throw new UnsupportedOperationException("viewAll operation not supported");
    }


    default Page<BoardCommonResponse> searchWithUser(int page, int size, String title,User user) {
        throw new UnsupportedOperationException("search operation not supported");
    }



    // 게시글 삭제
    void delete(User user, Long boardId);
    List<Board> findByUserId(Long userId);
}


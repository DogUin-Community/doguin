package com.sparta.doguin.domain.board.service;

import com.sparta.doguin.domain.board.dto.BoardRequest.BoardCommonRequest;
import com.sparta.doguin.domain.board.dto.BoardResponse;
import com.sparta.doguin.domain.board.dto.BoardResponse.BoardCommonResponse;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {

    // 게시글 생성
    void create(User user, BoardCommonRequest boardRequest, List<MultipartFile> files);

    // 게시글 수정
    void update(User user, Long boardId, BoardCommonRequest boardRequest,List<MultipartFile> files);

    // 게시글 단일 조회
    BoardResponse.BoardWithAnswer viewOneWithUser(Long boardId,User user);

    // 게시글 전체 조회
    default Page<BoardCommonResponse> viewAll(int page, int size) {
        throw new UnsupportedOperationException("viewAll operation not supported");
    }

    // 게시글 검색
    default Page<BoardCommonResponse> search(int page, int size, String title) {
        throw new UnsupportedOperationException("search operation not supported");
    }

    // 특정 작성자의 게시물 반환
    default Page<BoardCommonResponse> viewAllWithUser(int page, int size,User user) {
        throw new UnsupportedOperationException("viewAll operation not supported");
    }

    // 특정 작성자가 작성한 문의 게시물
    default Page<BoardCommonResponse> searchWithUser(int page, int size, String title,User user) {
        throw new UnsupportedOperationException("search operation not supported");
    }

    // 게시글 삭제
    void delete(User user, Long boardId);
    Page<Board> findByUserId(Long userId);

    default Page<Long> viewPopular(int page, int size){
        throw new UnsupportedOperationException("viewAll popular operation not supported");
    }
}


package com.sparta.doguin.domain.bookmark.service;

import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import com.sparta.doguin.domain.bookmark.entity.Bookmark;
import com.sparta.doguin.domain.bookmark.model.BookmarkRequest;
import com.sparta.doguin.domain.bookmark.model.BookmarkResponse;
import com.sparta.doguin.domain.bookmark.repository.BookmarkRepository;
import com.sparta.doguin.domain.common.exception.BookmarkException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sparta.doguin.domain.common.response.ApiResponseBookmarkEnum.BOOKMARK_NOT_FOUND;
import static com.sparta.doguin.domain.common.response.ApiResponseBookmarkEnum.BOOKMARK_OK;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    /**
     * 북마크 추가 or 삭제 메서드
     *
     * @param reqDto   / 생성할 북마크 데이터
     * @param authUser / 북마크 생성할 유저
     * @return ApiResponse<Void> / 성공 응답 반환
     * @author 김경민
     * @since 1.1
     */
    @Transactional
    @Override
    public ApiResponse<Void> togleBookmark(BookmarkRequest.BookmarkRequestCreate reqDto, AuthUser authUser) {
        User user = User.fromAuthUser(authUser);
        Optional<Bookmark> findBookmark = bookmarkRepository.findBookmarkByTargetIdAndBookmarkTargetType(reqDto.targetId(), reqDto.target());
        // 값이 존재한다면
        if (findBookmark.isPresent()) {
            bookmarkRepository.delete(findBookmark.get());
        } else {
            // 비어있다면
            Bookmark bookmark = Bookmark.builder()
                    .user(user)
                    .targetId(reqDto.targetId())
                    .target(reqDto.target())
                    .build();
            bookmarkRepository.save(bookmark);
        }

        return ApiResponse.of(BOOKMARK_OK);
    }

    /**
     * 특정 유저에 대한 북마크들
     *
     * @param pageable / 페이지 데이터 (페이지,사이즈,정렬)
     * @return ApiResponse<List < BookmarkDto>> / 북마크들 반환
     * @author 김경민
     * @since 1.0
     */
    @Transactional(readOnly = true)
    @Override
    public ApiResponse<Page<BookmarkResponse>> getAllBookmarksByUser(Pageable pageable, AuthUser authUser, BookmarkTargetType target) {
        User user = User.fromAuthUser(authUser);
        Page<Bookmark> pageableBookmarks;
        if (target == null) {
            pageableBookmarks = bookmarkRepository.findBookmarkByUser(user, pageable);
        } else {
            pageableBookmarks = bookmarkRepository.findBookmarkByUserAndTarget(user, pageable, target);
        }

        Page<BookmarkResponse> bookmarks = pageableBookmarks.map(BookmarkResponse.BookmarkResponseGet::of);
        return ApiResponse.of(BOOKMARK_OK, bookmarks);
    }

    /**
     * ID로 북마크 찾는 메서드
     *
     * @param bookmarkId / 찾을 북마크 ID
     * @return Bookmark / 찾은 북마크 데이터 반환
     * @throws BookmarkException / 북마크 없을시 예외처리
     * @author 김경민
     * @since 1.0
     */
    @Transactional(readOnly = true)
    public Bookmark findById(Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId).orElseThrow(() -> new BookmarkException(BOOKMARK_NOT_FOUND));
    }
}

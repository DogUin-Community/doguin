package com.sparta.doguin.domain.bookmark.service;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import com.sparta.doguin.domain.bookmark.entity.Bookmark;
import com.sparta.doguin.domain.bookmark.model.BookmarkDto;
import com.sparta.doguin.domain.bookmark.repository.BookmarkRepository;
import com.sparta.doguin.domain.common.exception.BookmarkException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.doguin.domain.common.response.ApiResponseBookmarkEnum.BOOKMARK_NOT_FOUND;
import static com.sparta.doguin.domain.common.response.ApiResponseBookmarkEnum.BOOKMARK_OK;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    // TODO: 이거 나중에 뺴야함
    private final UserRepository userRepository;

    /**
     * 특정 북마크 조회 메서드
     *
     * @param bookmarkId / 조회할 북마크 아이디
     * @return ApiResponse<BookmarkDto> / 반환할 북마크 데이터
     * @since 1.0
     * @author 김경민
     * @throws BookmarkException / 북마크 없을시 예외처리
     */
    @Transactional(readOnly = true)
    @Override
    public ApiResponse<BookmarkDto> getBookmark(Long bookmarkId) {
        Bookmark bookmark = findById(bookmarkId);
        BookmarkDto bookmarkDto = BookmarkDto.BookmarkResponse.of(bookmark);
        return ApiResponse.of(BOOKMARK_OK,bookmarkDto);
    }

    // TODO: 유저 찾는 메서드 변경해야함

    /**
     * 북마크 생성 메서드
     *
     * @param reqDto / 생성할 북마크 데이터
     * @param authUser / 북마크 생성할 유저
     * @return ApiResponse<Void> / 성공 응답 반환
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    @Override
    public ApiResponse<Void> createBookmark(BookmarkDto.BookmarkRequest reqDto, AuthUser authUser) {
        User user = userRepository.findById(Long.parseLong(authUser.getUserId())).orElseThrow();
        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .targetId(reqDto.targetId())
                .target(reqDto.target())
                .build();
        bookmarkRepository.save(bookmark);
        return ApiResponse.of(BOOKMARK_OK);
    }

    /**
     * 북마크 취소 메서드
     *
     * @param matchingId / 취소할 북마크 ID
     * @return ApiResponse<Void> / 성공 응답 반환
     * @throws BookmarkException / 북마크 없을시 예외처리
     * @since 1.0
     * @author 김경민
     */
    @Transactional(readOnly = true)
    @Override
    public ApiResponse<Void> deleteBookmark(Long matchingId) {
        Bookmark bookmark = findById(matchingId);
        bookmarkRepository.delete(bookmark);
        return ApiResponse.of(BOOKMARK_OK);
    }

    /**
     * 특정 유저에 대한 북마크들
     *
     * @param pageable / 페이지 데이터 (페이지,사이즈,정렬)
     * @return ApiResponse<List<BookmarkDto>> / 북마크들 반환
     * @since 1.0
     * @author 김경민
     */
    @Transactional(readOnly = true)
    @Override
    public ApiResponse<Page<BookmarkDto>> getAllBookmarksByUser(Pageable pageable, AuthUser authUser, BookmarkTargetType target) {
        User user = userRepository.findById(Long.parseLong(authUser.getUserId())).orElseThrow();
        Page<Bookmark> pageableBookmarks;
        if (target == null) {
            pageableBookmarks = bookmarkRepository.findBookmarkByUser(user, pageable);
        } else {
            pageableBookmarks = bookmarkRepository.findBookmarkByUserAndTarget(user,pageable,target);
        }

        Page<BookmarkDto> bookmarks = pageableBookmarks.map(BookmarkDto.BookmarkResponse::of);
        return ApiResponse.of(BOOKMARK_OK,bookmarks);
    }

    /**
     * ID로 북마크 찾는 메서드
     *
     * @param bookmarkId / 찾을 북마크 ID
     * @return Bookmark / 찾은 북마크 데이터 반환
     * @throws BookmarkException / 북마크 없을시 예외처리
     */
    @Transactional(readOnly = true)
    public Bookmark findById(Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId).orElseThrow(() -> new BookmarkException(BOOKMARK_NOT_FOUND));
    }
}

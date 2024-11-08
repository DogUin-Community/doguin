package com.sparta.doguin.domain.bookmark.service;

import com.sparta.doguin.domain.discussions.service.DiscussionService;
import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import com.sparta.doguin.domain.bookmark.entity.Bookmark;
import com.sparta.doguin.domain.bookmark.model.BookmarkRequest;
import com.sparta.doguin.domain.bookmark.model.BookmarkResponse;
import com.sparta.doguin.domain.bookmark.repository.BookmarkRepository;
import com.sparta.doguin.domain.bookmark.validator.BookmarkValidator;
import com.sparta.doguin.domain.common.exception.BookmarkException;
import com.sparta.doguin.domain.common.exception.HandleNotFound;
import com.sparta.doguin.domain.common.exception.OutsourcingException;
import com.sparta.doguin.domain.common.exception.ValidatorException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.service.OutsourcingServiceImpl;
import com.sparta.doguin.domain.question.service.QuestionService;
import com.sparta.doguin.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.doguin.domain.common.response.ApiResponseBookmarkEnum.*;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final OutsourcingServiceImpl outsourcingService;
    private final QuestionService questionService;
    private final DiscussionService discussionService;

    /**
     * 북마크 생성 메서드
     *
     * @param reqDto   / 생성할 북마크 데이터
     * @param authUser / 북마크 생성할 유저
     * @return ApiResponse<Void> / 성공 응답 반환
     * @throws OutsourcingException / 외주가 존재하지않을때 발생되는 예외
     * @throws HandleNotFound       질문이 존재하지 않을 경우 발생
     * @author 김경민
     * @since 1.0
     */
    @Transactional
    @Override
    public ApiResponse<Void> createBookmark(BookmarkRequest.BookmarkRequestCreate reqDto, AuthUser authUser) {
        User user = User.fromAuthUser(authUser);
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
     * @param bookmarkId / 취소할 북마크 ID
     * @return ApiResponse<Void> / 성공 응답 반환
     * @throws BookmarkException  / 북마크 없을시 예외처리
     * @throws ValidatorException / 북마크 한 사람이 본인이 아닐경우 예외처리
     * @author 김경민
     * @since 1.0
     */
    @Transactional
    @Override
    public ApiResponse<Void> deleteBookmark(Long bookmarkId, AuthUser authUser) {
        User user = User.fromAuthUser(authUser);
        Bookmark bookmark = findById(bookmarkId);
        BookmarkValidator.isMe(user.getId(), bookmark.getUser().getId());
        bookmarkRepository.delete(bookmark);
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

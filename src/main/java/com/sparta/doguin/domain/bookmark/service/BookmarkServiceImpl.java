package com.sparta.doguin.domain.bookmark.service;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.bookmark.entity.Bookmark;
import com.sparta.doguin.domain.bookmark.model.BookmarkDto;
import com.sparta.doguin.domain.bookmark.repository.BookmarkRepository;
import com.sparta.doguin.domain.common.exception.BookmarkException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.sparta.doguin.domain.common.response.ApiResponseBookmarkEnum.BOOKMARK_NOT_FOUND;
import static com.sparta.doguin.domain.common.response.ApiResponseBookmarkEnum.BOOKMARK_OK;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    // TODO: 이거 나중에 뺴야함
    private final UserRepository userRepository;

    @Override
    public ApiResponse<BookmarkDto> getBookmark(Long bookmarkId) {
        Bookmark bookmark = findById(bookmarkId);
        BookmarkDto bookmarkDto = BookmarkDto.BookmarkResponse.of(bookmark);
        return ApiResponse.of(BOOKMARK_OK,bookmarkDto);
    }

    // TODO: 유저 찾는 메서드 변경해야함
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

    @Override
    public ApiResponse<Void> deleteBookmark(Long matchingId) {
        Bookmark bookmark = findById(matchingId);
        bookmarkRepository.delete(bookmark);
        return ApiResponse.of(BOOKMARK_OK);
    }

    public Bookmark findById(Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId).orElseThrow(() -> new BookmarkException(BOOKMARK_NOT_FOUND));
    }
}

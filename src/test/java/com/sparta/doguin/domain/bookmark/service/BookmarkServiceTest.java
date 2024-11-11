package com.sparta.doguin.domain.bookmark.service;

import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import com.sparta.doguin.domain.bookmark.entity.Bookmark;
import com.sparta.doguin.domain.bookmark.model.BookmarkRequest;
import com.sparta.doguin.domain.bookmark.model.BookmarkResponse;
import com.sparta.doguin.domain.bookmark.repository.BookmarkRepository;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
import com.sparta.doguin.domain.outsourcing.service.OutsourcingServiceImpl;
import com.sparta.doguin.domain.question.entity.Question;
import com.sparta.doguin.domain.question.service.QuestionService;
import com.sparta.doguin.domain.setup.DataUtil;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.security.AuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    BookmarkRepository bookmarkRepository;

    @Mock
    OutsourcingServiceImpl outsourcingService;

    @Mock
    QuestionService questionService;

    @InjectMocks
    BookmarkServiceImpl bookmarkService;

    Pageable pageable;
    User user1;
    User user2;
    AuthUser authUser1;
    AuthUser authUser2;
    Long bookmarkId1;
    Long bookmarkId2;
    Bookmark outsourcingBookmark1;
    Bookmark outsourcingBookmark2;
    Bookmark questionBookmark1;
    Bookmark questionBookmark2;
    BookmarkRequest.BookmarkRequestCreate outsourcingBookmarkRequestCreate1;
    BookmarkRequest.BookmarkRequestCreate outsourcingBookmarkRequestCreate2;
    BookmarkRequest.BookmarkRequestCreate questionBookmarkRequestCreate1;
    BookmarkRequest.BookmarkRequestCreate questionBookmarkRequestCreate2;
    Outsourcing outsourcing1;
    Question question1;
    BookmarkTargetType outsourcingBookmarkTarget1;
    BookmarkTargetType outsourcingBookmarkTarget2;


    @BeforeEach
    void setUp() {
        pageable = DataUtil.pageable();
        user1 = DataUtil.user1();
        user2 = DataUtil.user2();
        authUser1 = DataUtil.authUser1();
        authUser2 = DataUtil.authUser2();
        outsourcingBookmark1 = DataUtil.outsourcingBookmark1();
        outsourcingBookmark2 = DataUtil.outsourcingBookmark2();
        questionBookmark1 = DataUtil.questionBookmark1();
        questionBookmark2 = DataUtil.questionBookmark2();
        outsourcingBookmarkRequestCreate1 = DataUtil.outsourcingBookmarkRequestCreate1();
        outsourcingBookmarkRequestCreate2 = DataUtil.outsourcingBookmarkRequestCreate2();
        questionBookmarkRequestCreate1 = DataUtil.questionBookmarkRequestCreate1();
        questionBookmarkRequestCreate2 = DataUtil.questionBookmarkRequestCreate1();
        outsourcing1 = DataUtil.outsourcing1();
        question1 = DataUtil.question1();
        bookmarkId1 = DataUtil.one();
        bookmarkId2 = DataUtil.two();
        outsourcingBookmarkTarget1 = outsourcingBookmarkRequestCreate1.target();
        outsourcingBookmarkTarget2 = questionBookmarkRequestCreate1.target();
    }

    @Nested
    public class 북마크_생성_테스트 {
        @Test
        @DisplayName("북마크 생성 성공 _ 외주")
        void test1() {
            // given
            given(outsourcingService.findById(outsourcingBookmarkRequestCreate1.targetId())).willReturn(outsourcing1);

            // when
            bookmarkService.togleBookmark(outsourcingBookmarkRequestCreate1,authUser1);

            // then - 1번 호출됐는지와, 예상한 데이터와 실제 데이터가 일치하는지 검증
            Mockito.verify(bookmarkRepository, Mockito.times(1)).save(Mockito.argThat(bookmark ->
                    bookmark.getUser().getId().equals(authUser1.getUserId()) &&
                    bookmark.getTargetId().equals(outsourcingBookmarkRequestCreate1.targetId()) &&
                    bookmark.getTarget().equals(outsourcingBookmarkRequestCreate1.target())
            ));
        }

        @Test
        @DisplayName("북마크 생성 성공 _ 잘문")
        void test2() {
            // given
            given(questionService.findById(questionBookmarkRequestCreate1.targetId())).willReturn(question1);

            // when
            bookmarkService.togleBookmark(questionBookmarkRequestCreate1,authUser1);

            // then - 1번 호출됐는지와, 예상한 데이터와 실제 데이터가 일치하는지 검증
            Mockito.verify(bookmarkRepository, Mockito.times(1)).save(Mockito.argThat(bookmark ->
                    bookmark.getUser().getId().equals(authUser1.getUserId()) &&
                            bookmark.getTargetId().equals(questionBookmarkRequestCreate1.targetId()) &&
                            bookmark.getTarget().equals(questionBookmarkRequestCreate1.target())
            ));
        }
    }


    @Nested
    public class 북마크_다건_조회_테스트 {
        @Test
        @DisplayName("로그인 되있는 유저의 북마크 목록 가져오기 성공 _ 외주")
        void test() {
            // given
            List<Bookmark> bookmarks = List.of(outsourcingBookmark1,outsourcingBookmark2);
            Page<Bookmark> bookmarkPages = new PageImpl<>(bookmarks,pageable,bookmarks.size());
            given(bookmarkRepository.findBookmarkByUserAndTarget(any(),any(),any())).willReturn(bookmarkPages);

            // when
            ApiResponse<Page<BookmarkResponse>> actual = bookmarkService.getAllBookmarksByUser(pageable,authUser1,outsourcingBookmarkTarget1);
            List<BookmarkResponse> actualDatas = actual.getData().getContent();
            List<BookmarkResponse.BookmarkResponseGet> actualDataConvert = actualDatas.stream()
                    .map(actualData -> (BookmarkResponse.BookmarkResponseGet) actualData)
                    .toList();
            // then - 예상한 데이터와, 실제 데이터의 값이 일치하는지 검증
            assertEquals( actualDatas.size(), bookmarks.size() );
            assertEquals(outsourcingBookmark1.getId(), actualDataConvert.get(0).id());
            assertEquals(outsourcingBookmark1.getUser().getId(), actualDataConvert.get(0).userId());
            assertEquals(outsourcingBookmark1.getTarget(), actualDataConvert.get(0).target());
            assertEquals(outsourcingBookmark1.getTargetId(), actualDataConvert.get(0).targetId());
            assertEquals(outsourcingBookmark2.getId(), actualDataConvert.get(1).id());
            assertEquals(outsourcingBookmark2.getUser().getId(), actualDataConvert.get(1).userId());
            assertEquals(outsourcingBookmark2.getTarget(), actualDataConvert.get(1).target());
            assertEquals(outsourcingBookmark2.getTargetId(), actualDataConvert.get(1).targetId());
        }

        @Test
        @DisplayName("로그인 되있는 유저의 북마크 목록 가져오기 성공 _ 외주,질문")
        void test2() {
            // given
            List<Bookmark> bookmarks = List.of(outsourcingBookmark1,outsourcingBookmark2);
            Page<Bookmark> bookmarkPages = new PageImpl<>(bookmarks,pageable,bookmarks.size());
            given(bookmarkRepository.findBookmarkByUser(any(),any())).willReturn(bookmarkPages);

            // when
            ApiResponse<Page<BookmarkResponse>> actual = bookmarkService.getAllBookmarksByUser(pageable,authUser1,null);
            List<BookmarkResponse> actualDatas = actual.getData().getContent();
            List<BookmarkResponse.BookmarkResponseGet> actualDataConvert = actualDatas.stream()
                    .map(actualData -> (BookmarkResponse.BookmarkResponseGet) actualData)
                    .toList();
            // then - 예상한 데이터와, 실제 데이터의 값이 일치하는지 검증
            assertEquals( actualDatas.size(), bookmarks.size() );
            assertEquals(outsourcingBookmark1.getId(), actualDataConvert.get(0).id());
            assertEquals(outsourcingBookmark1.getUser().getId(), actualDataConvert.get(0).userId());
            assertEquals(outsourcingBookmark1.getTarget(), actualDataConvert.get(0).target());
            assertEquals(outsourcingBookmark1.getTargetId(), actualDataConvert.get(0).targetId());
            assertEquals(outsourcingBookmark2.getId(), actualDataConvert.get(1).id());
            assertEquals(outsourcingBookmark2.getUser().getId(), actualDataConvert.get(1).userId());
            assertEquals(outsourcingBookmark2.getTarget(), actualDataConvert.get(1).target());
            assertEquals(outsourcingBookmark2.getTargetId(), actualDataConvert.get(1).targetId());
        }
    }
}
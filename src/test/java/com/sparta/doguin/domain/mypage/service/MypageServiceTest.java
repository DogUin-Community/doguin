package com.sparta.doguin.domain.mypage.service;

import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.board.service.BulletinService;
import com.sparta.doguin.domain.board.service.InquiryService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseMypageEnum;
import com.sparta.doguin.domain.follow.service.FollowService;
import com.sparta.doguin.domain.mypage.dto.MypageResponse;
import com.sparta.doguin.domain.question.entity.Question;
import com.sparta.doguin.domain.question.service.QuestionService;
import com.sparta.doguin.domain.setup.DataUtil;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MypageServiceTest {

    @Mock
    FollowService followService;

    @Mock
    UserService userService;

    @Mock
    QuestionService questionService;

    @Mock
    BulletinService bulletinService;

    @Mock
    InquiryService inquiryService;

    @InjectMocks
    MypageService mypageService;

    AuthUser authUser;
    User user;
    List<Board> bulletinBoardList;
    List<Question> questionList;
    List<Board> inquiryBoardList;

    @BeforeEach
    void setUp() {
        authUser = DataUtil.authUser1();
        user = DataUtil.user1();
        bulletinBoardList = DataUtil.bulletinBoardPage().getContent();
        questionList = List.of(DataUtil.question1(), DataUtil.question2());
        inquiryBoardList = DataUtil.inquiryBoardPage().getContent();
    }

    @Test
    @DisplayName("마이페이지 정보 조회 성공")
    void getMypage_success() {
        // given
        given(userService.findById(authUser.getUserId())).willReturn(user);
        given(followService.getFollowedCount(user.getId())).willReturn(5L);
        given(followService.getFollowerCount(user.getId())).willReturn(3L);
        given(bulletinService.findByUserId(user.getId())).willReturn(DataUtil.bulletinBoardPage());
        given(questionService.findAllByUserId(authUser)).willReturn(questionList);
        given(inquiryService.findByUserId(user.getId())).willReturn(DataUtil.inquiryBoardPage());

        // when
        MypageResponse.Mypage actual = mypageService.getMypage(authUser);

        // then
        assertEquals("test1@naver.com", actual.email());
        assertEquals("testNickname1", actual.nickname());
        assertEquals(5L, actual.followedCount());
        assertEquals(3L, actual.followerCount());
        assertEquals(List.of("Bulletin Title 1", "Bulletin Title 2"), actual.boards());
        assertEquals(List.of("test title", "test title1"), actual.questions());
        assertEquals(List.of("Inquiry Title 1", "Inquiry Title 2"), actual.inquiries());
    }
}

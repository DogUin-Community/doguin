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
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final FollowService followService;
    private final UserService userService;
    private final QuestionService questionService;
    private final BulletinService bulletinService;
    private final InquiryService inquiryService;

    /**
     * 사용자의 마이페이지 정보를 조회하는 메서드
     *
     * @param authUser 로그인한 사용자의 인증 정보
     * @return MypageResponse.Mypage에 담긴 마이페이지 정보들(팔로우한 사람들의 수, 작성한 게시들글 등)
     * @since 1.0
     * @author 황윤서
     */
    public ApiResponse<MypageResponse.Mypage> getMypage(AuthUser authUser) { //ApiResponse<MypageResponse>
        User user = userService.findById(authUser.getUserId());

        // 나를 팔로우한 사람들 수 조회
        long followedCount = followService.getFollowedCount(user.getId());

        // 내가 팔로우한 사람들의 수 조회
        long followerCount = followService.getFollowerCount(user.getId());

        // 작성한 Bulletin 게시글들의 타이틀만 조회
        List<String> bulletinBoards = bulletinService.findByUserId(user.getId())
                .stream()
                .map(Board::getTitle)
                .toList();

        // 작성한 질문글들의 타이틀만 조회
        List<String> questions = questionService.findAllByUserId(authUser)
                .stream()
                .map(Question::getTitle)
                .toList();

        // 작성한 1대1문의글들의 타이틀만 조회
        List<String> inquiryBoards = inquiryService.findByUserId(user.getId())
                .stream()
                .map(Board::getTitle)
                .toList();

        MypageResponse.Mypage getMypage = new MypageResponse.Mypage(
                user.getEmail(),
                user.getNickname(),
                followedCount,
                followerCount,
                bulletinBoards,
                questions,
                inquiryBoards
        );

        return ApiResponse.of(ApiResponseMypageEnum.MYPAGE_GET_SUCCESS, getMypage);
    }
}
package com.sparta.doguin.domain.mypage.dto;

import java.util.List;

public sealed interface MypageResponse permits MypageResponse.Mypage {
    record Mypage(
            String email,
            String nickname,
            long followedCount,
            long followerCount,
            List<String> boards,
            List<String> questions,
            List<String> inquiries
    ) implements MypageResponse {}
}

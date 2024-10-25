package com.sparta.doguin.domain.setup;

import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.board.BoardType;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import com.sparta.doguin.domain.bookmark.entity.Bookmark;
import com.sparta.doguin.domain.bookmark.model.BookmarkRequest;
import com.sparta.doguin.domain.matching.constans.MathingStatusType;
import com.sparta.doguin.domain.matching.entity.Matching;
import com.sparta.doguin.domain.matching.model.MatchingRequest;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingRequest;
import com.sparta.doguin.domain.portfolio.entity.Portfolio;
import com.sparta.doguin.domain.portfolio.model.PortfolioRequest;
import com.sparta.doguin.domain.question.dto.QuestionRequest;
import com.sparta.doguin.domain.question.entity.Question;
import com.sparta.doguin.domain.question.enums.FirstCategory;
import com.sparta.doguin.domain.question.enums.LastCategory;
import com.sparta.doguin.domain.question.enums.SecondCategory;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;

public class DataUtil {
    public static Pageable pageable() {
        return PageRequest.of(0,10, Sort.by(Sort.Direction.DESC,"createdAt"));
    }

    public static User user1() {
        return new User(
                 one(),
                "test@naver.com",
                "!@Skdud340",
                "testNickname",
                UserType.INDIVIDUAL,
                UserRole.ROLE_USER,"","","","",""
        );
    }

    public static User user2() {
        return new User(
                 two(),
                "test1@naver.com",
                "!@Skdud340",
                "testNickname1",
                UserType.INDIVIDUAL,
                UserRole.ROLE_USER,"","","","",""
        );
    }

    public static Long one(){
        return 1L;
    }

    public static Long two(){
        return 2L;
    }

    public static AuthUser authUser1() {
        return new AuthUser(
                user1().getId(),
                user1().getEmail(),
                user1().getNickname(),
                user1().getUserType(),
                user1().getUserRole()
        );
    }

    public static AuthUser authUser2() {
        return new AuthUser(
                user2().getId(),
                user2().getEmail(),
                user2().getNickname(),
                user2().getUserType(),
                user2().getUserRole()
        );
    }

    public static OutsourcingRequest.OutsourcingRequestCreate outsourctingRequestCreate1(){
        return new OutsourcingRequest.OutsourcingRequestCreate(
                "Mobile App Development",
                "Looking for a mobile app developer to build an e-commerce app.",
                "Experience with Flutter is preferred.",
                "Remote",
                10000L,
                LocalDateTime.of(2024, 10, 1, 9, 0, 0),
                LocalDateTime.of(2024, 10, 31, 9, 0, 0),
                LocalDateTime.of(2024, 11, 10, 9, 0, 0),
                LocalDateTime.of(2025, 10, 1, 9, 0, 0),
                AreaType.SEOUL
        );
    }

    public static OutsourcingRequest.OutsourcingRequestCreate outsourctingRequestCreate2(){
        return new OutsourcingRequest.OutsourcingRequestCreate(
                "Mobile App Development1",
                "Looking for a mobile app developer to build an e-commerce app1.",
                "Experience with Flutter is preferred1.",
                "Remote1",
                100001L,
                LocalDateTime.of(2024, 10, 1, 9, 0, 0),
                LocalDateTime.of(2024, 10, 31, 9, 0, 0),
                LocalDateTime.of(2024, 11, 10, 9, 0, 0),
                LocalDateTime.of(2025, 10, 1, 9, 0, 0),
                AreaType.SEOUL
        );
    }

    public static OutsourcingRequest.OutsourcingRequestUpdate outsourctingRequestUpdate1(){
        return new OutsourcingRequest.OutsourcingRequestUpdate(
                "Mobile App Development update1",
                "Looking for a mobile app developer to build an e-commerce app. update1",
                "Experience with Flutter is preferred. update1",
                "Remote update1",
                100222L,
                LocalDateTime.of(2024, 10, 1, 9, 0, 0),
                LocalDateTime.of(2024, 10, 31, 9, 0, 0),
                LocalDateTime.of(2024, 11, 10, 9, 0, 0),
                LocalDateTime.of(2025, 10, 1, 9, 0, 0),
                AreaType.SEOUL
        );
    }

    public static OutsourcingRequest.OutsourcingRequestUpdate outsourctingRequestUpdate2(){
        return new OutsourcingRequest.OutsourcingRequestUpdate(
                "Mobile App Development update2",
                "Looking for a mobile app developer to build an e-commerce app. update2",
                "Experience with Flutter is preferred. update2",
                "Remote update2",
                100222L,
                LocalDateTime.of(2024, 10, 1, 9, 0, 0),
                LocalDateTime.of(2024, 10, 31, 9, 0, 0),
                LocalDateTime.of(2024, 11, 10, 9, 0, 0),
                LocalDateTime.of(2025, 10, 1, 9, 0, 0),
                AreaType.SEOUL
        );
    }

    public static Outsourcing outsourcing1(){
        return Outsourcing.builder()
                .user(user1())
                .id(one())
                .title(outsourctingRequestCreate1().title())
                .content(outsourctingRequestCreate1().content())
                .preferential(outsourctingRequestCreate1().preferential())
                .work_type(outsourctingRequestCreate1().work_type())
                .price(outsourctingRequestCreate1().price())
                .recruit_start_date(outsourctingRequestCreate1().recruit_start_date())
                .recruit_end_date(outsourctingRequestCreate1().recruit_end_date())
                .work_start_date(outsourctingRequestCreate1().work_start_date())
                .work_end_date(outsourctingRequestCreate1().work_end_date())
                .area(outsourctingRequestCreate1().area())
                .build();
    }

    public static Outsourcing outsourcing2(){
        return Outsourcing.builder()
                .user(user1())
                .id(two())
                .title(outsourctingRequestCreate2().title())
                .content(outsourctingRequestCreate2().content())
                .preferential(outsourctingRequestCreate2().preferential())
                .work_type(outsourctingRequestCreate2().work_type())
                .price(outsourctingRequestCreate2().price())
                .recruit_start_date(outsourctingRequestCreate2().recruit_start_date())
                .recruit_end_date(outsourctingRequestCreate2().recruit_end_date())
                .work_start_date(outsourctingRequestCreate2().work_start_date())
                .work_end_date(outsourctingRequestCreate2().work_end_date())
                .area(outsourctingRequestCreate2().area())
                .build();
    }

    public static BookmarkRequest.BookmarkRequestCreate outsourcingBookmarkRequestCreate1(){
        return new BookmarkRequest.BookmarkRequestCreate(
                one(),
                BookmarkTargetType.OUTSOURCING
        );
    }

    public static BookmarkRequest.BookmarkRequestCreate outsourcingBookmarkRequestCreate2(){
        return new BookmarkRequest.BookmarkRequestCreate(
                two(),
                BookmarkTargetType.OUTSOURCING
        );
    }

    public static BookmarkRequest.BookmarkRequestCreate questionBookmarkRequestCreate1(){
        return new BookmarkRequest.BookmarkRequestCreate(
                one(),
                BookmarkTargetType.QUESTION
        );
    }

    public static BookmarkRequest.BookmarkRequestCreate questionBookmarkRequestCreate2(){
        return new BookmarkRequest.BookmarkRequestCreate(
                two(),
                BookmarkTargetType.QUESTION
        );
    }

    public static Bookmark outsourcingBookmark1(){
        return new Bookmark(
                one(),
                user1(),
                one(),
                BookmarkTargetType.OUTSOURCING
        );
    }

    public static Bookmark outsourcingBookmark2(){
        return new Bookmark(
                two(),
                user2(),
                two(),
                BookmarkTargetType.OUTSOURCING
        );
    }

    public static Bookmark questionBookmark1(){
        return new Bookmark(
                one(),
                user1(),
                one(),
                BookmarkTargetType.QUESTION
        );
    }

    public static Bookmark questionBookmark2(){
        return new Bookmark(
                two(),
                user2(),
                two(),
                BookmarkTargetType.QUESTION
        );
    }

    public static QuestionRequest.CreatedQuestion questionRequestCreate1(){
        return new QuestionRequest.CreatedQuestion(
                "test title",
                "test content",
                FirstCategory.JAVA,
                SecondCategory.STRING,
                LastCategory.REDIS
        );
    }

    public static QuestionRequest.CreatedQuestion questionRequestCreate2(){
        return new QuestionRequest.CreatedQuestion(
                "test title1",
                "test content1",
                FirstCategory.JAVA,
                SecondCategory.STRING,
                LastCategory.REDIS
        );
    }

    public static Question question1(){
        return new Question(
                one(),
                questionRequestCreate1().title(),
                questionRequestCreate1().content(),
                questionRequestCreate1().firstCategory(),
                questionRequestCreate1().secondCategory(),
                questionRequestCreate1().lastCategory()
        );
    }


    public static Question question2(){
        return new Question(
                two(),
                questionRequestCreate2().title(),
                questionRequestCreate2().content(),
                questionRequestCreate2().firstCategory(),
                questionRequestCreate2().secondCategory(),
                questionRequestCreate2().lastCategory()
        );
    }

    public static PortfolioRequest.PortfolioRequestCreate portfolioRequestCreate1(){
        return new PortfolioRequest.PortfolioRequestCreate(
                "Senior Developer Portfolio",
                "This portfolio highlights my experience in backend development with Java and Spring.",
                10L,
                "Remote",
                "Developed multiple microservices for an e-commerce platform.",
                AreaType.SEOUL
        );
    }

    public static PortfolioRequest.PortfolioRequestCreate portfolioRequestCreate2(){
        return new PortfolioRequest.PortfolioRequestCreate(
                "Senior Developer Portfolio1",
                "This portfolio highlights my experience in backend development with Java and Spring1.",
                11L,
                "Remote1",
                "Deve1loped multiple microservices for an e-commerce platform.",
                AreaType.SEOUL
        );
    }

    public static PortfolioRequest.PortfolioRequestUpdate portfolioRequestUpdate1(){
        return new PortfolioRequest.PortfolioRequestUpdate(
                "Senior Developer Portfolio11",
                "This portfolio highlight1s my experience in backend development with Java and Spring1.",
                5L,
                "Remot2e1",
                "Dev22e1loped multiple microservices for an e-commerce platform.",
                AreaType.SEOUL
        );
    }

    public static PortfolioRequest.PortfolioRequestUpdate portfolioRequestUpdate2(){
        return new PortfolioRequest.PortfolioRequestUpdate(
                "Senior Developer Portfolio33",
                "This portfolio highlight31s my experience in backend development with Java and Spring1.",
                4L,
                "Remot233e1",
                "Dev22333e1loped multiple microservices for an e-commerce platform.",
                AreaType.SEOUL
        );
    }

    public static Portfolio portfolio1(){
        return new Portfolio(
                one(),
                user1(),
                portfolioRequestCreate1().title(),
                portfolioRequestCreate1().content(),
                portfolioRequestCreate1().work_experience(),
                portfolioRequestCreate1().work_type(),
                portfolioRequestCreate1().proejct_history(),
                portfolioRequestCreate1().area()
        );
    }

    public static Portfolio portfolio2() {
        return new Portfolio(
                two(),
                user2(),
                portfolioRequestCreate2().title(),
                portfolioRequestCreate2().content(),
                portfolioRequestCreate2().work_experience(),
                portfolioRequestCreate2().work_type(),
                portfolioRequestCreate2().proejct_history(),
                portfolioRequestCreate2().area()
        );
    }

    public static MatchingRequest.MatchingRequestCreate matchingRequestCreate1(){
        return new MatchingRequest.MatchingRequestCreate(
                one(),
                one()
        );
    }

    public static MatchingRequest.MatchingRequestCreate matchingRequestCreate2(){
        return new MatchingRequest.MatchingRequestCreate(
                two(),
                two()
        );
    }

    public static MatchingRequest.MatchingRequestUpdate MatchingRequestUpdate1(){
        return new MatchingRequest.MatchingRequestUpdate(
                MathingStatusType.YES
        );
    }

    public static MatchingRequest.MatchingRequestUpdate MatchingRequestUpdate2(){
        return new MatchingRequest.MatchingRequestUpdate(
                MathingStatusType.NO
        );
    }

    public static Matching matching1() {
        return new Matching(
                one(),
                user1(),
                portfolio1(),
                outsourcing1(),
                MathingStatusType.READY
        );
    }

    public static Matching matching2() {
        return new Matching(
                two(),
                user2(),
                portfolio2(),
                outsourcing2(),
                MathingStatusType.READY
        );
    }

    public static Board bulletin1() {
        return new Board("Bulletin Title 1", "Bulletin Content 1", BoardType.BOARD_BULLETIN, user1());
    }

    public static Board bulletin2() {
        return new Board("Bulletin Title 2", "Bulletin Content 2", BoardType.BOARD_BULLETIN, user1());
    }

    public static Board inquiry1() {
        return new Board("Inquiry Title 1", "Inquiry Content 1", BoardType.BOARD_INQUIRY, user1());
    }

    public static Board inquiry2() {
        return new Board("Inquiry Title 2", "Inquiry Content 2", BoardType.BOARD_INQUIRY, user1());
    }

    // bulletinBoard의 Page 타입 반환 메서드 추가(마이페이지 조회 시 Board 객체는 Page타입을 반환하고 있어서 이 메서드를 추가하였음)
    public static Page<Board> bulletinBoardPage() {
        List<Board> bulletins = List.of(bulletin1(), bulletin2());
        return new PageImpl<>(bulletins);  // List를 Page로 변환
    }

    // inquirie의 Page 타입 반환 메서드 추가(마이페이지 조회 시 Board 객체는 Page타입을 반환하고 있어서 이 메서드를 추가하였음)
    public static Page<Board> inquiryBoardPage() {
        List<Board> inquiries = List.of(inquiry1(), inquiry2());
        return new PageImpl<>(inquiries);
    }
}

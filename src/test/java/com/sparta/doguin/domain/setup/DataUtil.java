package com.sparta.doguin.domain.setup;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.bookmark.constans.BookmarkTargetType;
import com.sparta.doguin.domain.bookmark.entity.Bookmark;
import com.sparta.doguin.domain.bookmark.model.BookmarkRequest;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingRequest;
import com.sparta.doguin.domain.question.dto.QuestionRequest;
import com.sparta.doguin.domain.question.entity.Question;
import com.sparta.doguin.domain.question.enums.FirstCategory;
import com.sparta.doguin.domain.question.enums.LastCategory;
import com.sparta.doguin.domain.question.enums.SecondCategory;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

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
                UserRole.ROLE_USER
        );
    }

    public static User user2() {
        return new User(
                 two(),
                "test1@naver.com",
                "!@Skdud340",
                "testNickname1",
                UserType.INDIVIDUAL,
                UserRole.ROLE_USER
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



}
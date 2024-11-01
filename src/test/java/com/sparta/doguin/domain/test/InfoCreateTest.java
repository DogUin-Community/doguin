package com.sparta.doguin.domain.test;

import com.sparta.doguin.domain.matching.constans.MathingStatusType;
import com.sparta.doguin.domain.matching.entity.Matching;
import com.sparta.doguin.domain.matching.repository.MatchingRepository;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
import com.sparta.doguin.domain.outsourcing.repository.OutsourcingRepository;
import com.sparta.doguin.domain.outsourcing.service.OutsourcingServiceImpl;
import com.sparta.doguin.domain.portfolio.entity.Portfolio;
import com.sparta.doguin.domain.portfolio.repository.PortfolioRepository;
import com.sparta.doguin.domain.portfolio.service.PortfolioServiceImpl;
import com.sparta.doguin.domain.user.dto.UserRequest;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;
import com.sparta.doguin.domain.user.service.AuthService;
import com.sparta.doguin.domain.user.service.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class InfoCreateTest {
    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @Autowired
    OutsourcingServiceImpl outsourcingService;

    @Autowired
    PortfolioServiceImpl portfolioService;

    @Autowired
    OutsourcingRepository outsourcingRepository;

    @Autowired
    PortfolioRepository portfolioRepository;

    @Autowired
    MatchingRepository matchingRepository;

    @Autowired
    EntityManager em;

    private static final int ROOP_COUNT = 1_000_00;
    private static final int BATCH_SIZE = 1_000;

    @Test
    void testInfoCreate() {
        userCreateTest();
        outSourcingCreateTest();
        portfoliCreateTest();
        matchingCreateTest();
    }

    @Test
    @Transactional
    void 외주_배치사이즈_한번에_만들기_Save() {
        userCreateTest();
        User user = userService.findById(1L);
        long start = System.currentTimeMillis();
        List<Outsourcing> outsourcingList = new ArrayList<>();
        for (int i = 0; i < ROOP_COUNT; i++) {
            Outsourcing outsourcing = new Outsourcing(
                    null,
                    user,
                    "Mobile App Development" + i,
                    "Looking for a mobile app developer to build an e-commerce app." + i,
                    "Experience with Flutter is preferred." + i,
                    "Remote",
                    10000L,
                    LocalDateTime.of(2024, 10, 10, 9, 0, 0),
                    LocalDateTime.of(2024, 10, 10, 9, 0, 0),
                    LocalDateTime.of(2024, 10, 10, 9, 0, 0),
                    LocalDateTime.of(2024, 10, 10, 9, 0, 0),
                    AreaType.SEOUL
            );
            outsourcingList.add(outsourcing);
        }
        outsourcingRepository.saveAll(outsourcingList);
        long end = System.currentTimeMillis();
        System.out.println("===========================================");
        System.out.println(end - start);
        System.out.println("===========================================");
    }

    @Test
    @Transactional
    void 외주_배치사이즈_한번에_만들기_SaveAll() {
        userCreateTest();
        User user = userService.findById(1L);
        long start = System.currentTimeMillis();
        List<Outsourcing> outsourcingList = new ArrayList<>();
        for (int i = 0; i < ROOP_COUNT; i++) {
            Outsourcing outsourcing = new Outsourcing(
                    null,
                    user,
                    "Mobile App Development" + i,
                    "Looking for a mobile app developer to build an e-commerce app." + i,
                    "Experience with Flutter is preferred." + i,
                    "Remote",
                    10000L,
                    LocalDateTime.of(2024, 10, 10, 9, 0, 0),
                    LocalDateTime.of(2024, 10, 10, 9, 0, 0),
                    LocalDateTime.of(2024, 10, 10, 9, 0, 0),
                    LocalDateTime.of(2024, 10, 10, 9, 0, 0),
                    AreaType.SEOUL
            );
            outsourcingList.add(outsourcing);
        }
        outsourcingRepository.saveAll(outsourcingList);
        long end = System.currentTimeMillis();
        System.out.println("===========================================");
        System.out.println(end - start);
        System.out.println("===========================================");
    }

    @Test
    @Transactional
    @Rollback(false)
    void 외주_배치사이즈_만들기() {
        userCreateTest();
        User user = userService.findById(1L);
        long start = System.currentTimeMillis();
        List<Outsourcing> outsourcingList = new ArrayList<>();
        for (int i = 0; i < ROOP_COUNT; i++) {
            Outsourcing outsourcing = new Outsourcing(
                    null,
                    user,
                    "Mobile App Development" + i,
                    "Looking for a mobile app developer to build an e-commerce app." + i,
                    "Experience with Flutter is preferred." + i,
                    "Remote",
                    10000L,
                    LocalDateTime.of(2024, 10, 10, 9, 0, 0),
                    LocalDateTime.of(2024, 10, 10, 9, 0, 0),
                    LocalDateTime.of(2024, 10, 10, 9, 0, 0),
                    LocalDateTime.of(2024, 10, 10, 9, 0, 0),
                    AreaType.SEOUL
            );
            outsourcingList.add(outsourcing);
            // 1000개가 쌓였다면
            if (outsourcingList.size() % BATCH_SIZE == 0) {
                outsourcingRepository.saveAll(outsourcingList);
                em.flush();
                em.clear();
                outsourcingList.clear();
            }
        }
        if (!outsourcingList.isEmpty()) {
            outsourcingRepository.saveAll(outsourcingList);
        }
        long end = System.currentTimeMillis();
        System.out.println("===========================================");
        System.out.println(end - start);
        System.out.println("===========================================");
    }

    @Test
    void 외주_그냥_만들기() {
        userCreateTest();
        User user = userService.findById(1L);
        long start = System.currentTimeMillis();
        for (int i = 0; i < ROOP_COUNT; i++) {
            Outsourcing outsourcing = new Outsourcing(
                    null,
                    user,
                    "Mobile App Development" + i,
                    "Looking for a mobile app developer to build an e-commerce app." + i,
                    "Experience with Flutter is preferred." + i,
                    "Remote",
                    10000L,
                    LocalDateTime.of(2024, 10, 10, 9, 0, 0),
                    LocalDateTime.of(2024, 10, 10, 9, 0, 0),
                    LocalDateTime.of(2024, 10, 10, 9, 0, 0),
                    LocalDateTime.of(2024, 10, 10, 9, 0, 0),
                    AreaType.SEOUL
            );
            outsourcingRepository.save(outsourcing);
        }
        long end = System.currentTimeMillis();
        System.out.println("===========================================");
        System.out.println(end - start);
        System.out.println("===========================================");
    }


    void userCreateTest() {
            UserRequest.Signup signup = new UserRequest.Signup(
                    "test@naver.com",
                    "!@Skdud340",
                    "testNickname",
                    UserType.COMPANY.name() ,
                    UserRole.ROLE_USER.name(),
                    "",
                    "testIntroduce",
                    "test HomdeAdress",
                    "test gitAddress",
                    "test blogAdress"
            );
            authService.signup(
                    signup,
                    null
            );
        outSourcingCreateTest();
    }
    void outSourcingCreateTest(){
        User user = userService.findById(1L);
        Outsourcing outsourcing = new Outsourcing(
                null,
                user,
                "Mobile App Development",
                "Looking for a mobile app developer to build an e-commerce app.",
                "Experience with Flutter is preferred.",
                "Remote",
                10000L,
                 LocalDateTime.of(2024,10,10,9,0,0),
                LocalDateTime.of(2024,10,10,9,0,0),
                LocalDateTime.of(2024,10,10,9,0,0),
                LocalDateTime.of(2024,10,10,9,0,0),
                AreaType.SEOUL
        );
        outsourcingRepository.save(outsourcing);
    }

    void portfoliCreateTest(){
        User user = userService.findById(1L);
        Portfolio portfolio = new Portfolio(
                null,
                user,
                "test",
                "test des",
                2L,
                "spring",
                "hists",
                AreaType.SEOUL
        );
        portfolioRepository.save(portfolio);
    }

    void matchingCreateTest(){
        User user = userService.findById(1L);
        Portfolio portfolio = portfolioService.findById(1L);
        Outsourcing outsourcing = outsourcingService.findById(1L);

        Matching matching = new Matching(
                user,
                portfolio,
                outsourcing,
                MathingStatusType.READY
        );
        matchingRepository.save(matching);

    }
}

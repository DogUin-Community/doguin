//package com.sparta.doguin.domain.test;
//
//import com.sparta.doguin.domain.outsourcing.constans.AreaType;
//import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
//import com.sparta.doguin.domain.outsourcing.repository.OutsourcingRepository;
//import com.sparta.doguin.domain.portfolio.entity.Portfolio;
//import com.sparta.doguin.domain.portfolio.repository.PortfolioRepository;
//import com.sparta.doguin.domain.user.entity.User;
//import com.sparta.doguin.domain.user.enums.UserRole;
//import com.sparta.doguin.domain.user.enums.UserType;
//import com.sparta.doguin.domain.user.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
//@SpringBootTest
//public class dumyData {
//    @Autowired
//    OutsourcingRepository outsourcingRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    PortfolioRepository portfolioRepository;
//
//    @Autowired
//    PasswordEncoder pe;
//
//
//    @Test
//    @Rollback(false)
//    @Transactional
//    public void makeData(){
//        for ( int i=1; i<=10; i++ ) {
//            String password = pe.encode("!@Skdud340");
//            User user = new User(
//                    null,
//                    "test" + i + "@naver.com",
//                    password,
//                    "testNickname" + i,
//                    UserType.INDIVIDUAL,
//                    UserRole.ROLE_USER,
//                    "",
//                    "testIntroduce" + i,
//                    "testHomeAdress",
//                    "testGitAdress",
//                    "testBlogAdress"
//            );
//            userRepository.save(user);
//            Outsourcing outsourcing = new Outsourcing(
//                    null,
//                    user,
//                    "testtitle" + i,
//                    "testContent" + i,
//                    "testPref" + i,
//                    "testWork" + i,
//                    10000L,
//                    LocalDateTime.of(2024, 10, 1, 9, 0, 0),
//                    LocalDateTime.of(2024, 10, 1, 9, 0, 0),
//                    LocalDateTime.of(2024, 10, 1, 9, 0, 0),
//                    LocalDateTime.of(2024, 10, 1, 9, 0, 0),
//                    AreaType.SEOUL,
//                    null,
//                    null
//            );
//            outsourcingRepository.save(outsourcing);
//            Portfolio portfolio = new Portfolio(
//                    null,
//                    user,
//                    "testT" + i,
//                    "testC" + i,
//                    10000L,
//                    "testWo" + i,
//                    "testPro" + i,
//                    AreaType.SEOUL
//            );
//            portfolioRepository.save(portfolio);
//        }
//    }
//}

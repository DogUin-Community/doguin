//package com.sparta.doguin.domain.test;
//
//import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
//import com.sparta.doguin.domain.outsourcing.repository.OutsourcingRepository;
//import com.sparta.doguin.domain.portfolio.entity.Portfolio;
//import com.sparta.doguin.domain.portfolio.repository.PortfolioRepository;
//import com.sparta.doguin.domain.setup.DataUtil;
//import com.sparta.doguin.domain.user.entity.User;
//import com.sparta.doguin.domain.user.enums.UserRole;
//import com.sparta.doguin.domain.user.enums.UserType;
//import com.sparta.doguin.domain.user.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
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
//    Outsourcing outsourcing;
//    Portfolio portfolio;
//
//    @BeforeEach
//    void setup(){
//        outsourcing = DataUtil.outsourcing1();
//        portfolio = DataUtil.portfolio1();
//    }
//
//    @Test
//    @Transactional
//    public void makeData(){
//        outsourcingRepository.save(outsourcing);
//        portfolioRepository.save(portfolio);
//        for ( int i=1; i<=100; i++ ) {
//            String password = pe.encode("!@Skdud340");
//            User user = new User(
//                    (long) i,
//                    "test@naver.com" + i,
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
//        }
//    }
//}

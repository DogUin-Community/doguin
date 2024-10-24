package com.sparta.doguin.domain.matching.service;

import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.matching.constans.MathingStatusType;
import com.sparta.doguin.domain.matching.entity.Matching;
import com.sparta.doguin.domain.matching.model.MatchingRequest;
import com.sparta.doguin.domain.matching.model.MatchingResponse;
import com.sparta.doguin.domain.matching.repository.MatchingRepository;
import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
import com.sparta.doguin.domain.outsourcing.service.OutsourcingServiceImpl;
import com.sparta.doguin.domain.portfolio.entity.Portfolio;
import com.sparta.doguin.domain.portfolio.service.PortfolioServiceImpl;
import com.sparta.doguin.domain.setup.DataUtil;
import com.sparta.doguin.domain.user.entity.User;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MatchingServiceImplTest {

    @Mock
    MatchingRepository matchingRepository;

    @Mock
    OutsourcingServiceImpl outsourcingService;

    @Mock
    PortfolioServiceImpl portfolioService;

    @InjectMocks
    MatchingServiceImpl matchingService;


    Pageable pageable;
    User user1;
    User user2;
    AuthUser authUser1;
    AuthUser authUser2;
    Long matchingId1;
    Long matchingId2;
    MatchingRequest.MatchingRequestCreate matchingRequestCreate1;
    MatchingRequest.MatchingRequestCreate matchingRequestCreate2;
    MatchingRequest.MatchingRequestUpdate matchingRequestUpdate1;
    MatchingRequest.MatchingRequestUpdate matchingRequestUpdate2;
    Matching matching1;
    Matching matching2;
    Outsourcing outsourcing1;
    Portfolio portfolio1;
    MathingStatusType status1;


    @BeforeEach
    void setUp() {
        pageable = DataUtil.pageable();
        user1 = DataUtil.user1();
        user2 = DataUtil.user2();
        authUser1 = DataUtil.authUser1();
        authUser2 = DataUtil.authUser2();
        matchingId1 = DataUtil.one();
        matchingId2 = DataUtil.two();
        matchingRequestCreate1 = DataUtil.matchingRequestCreate1();
        matchingRequestCreate2 = DataUtil.matchingRequestCreate2();
        matchingRequestUpdate1 = DataUtil.MatchingRequestUpdate1();
        matchingRequestUpdate2 = DataUtil.MatchingRequestUpdate2();
        matching1 = DataUtil.matching1();
        matching2 = DataUtil.matching2();
        outsourcing1 = DataUtil.outsourcing1();
        portfolio1 = DataUtil.portfolio1();
        status1 = matching1.getStatus();
    }

    @Nested
    public class 매칭_생성_테스트 {
        @Test
        void createMatching() {
            // given
            given(outsourcingService.findById(matchingRequestCreate1.outsourcingId())).willReturn(outsourcing1);
            given(portfolioService.findById(matchingRequestCreate1.portfolioId())).willReturn(portfolio1);

            // when
            matchingService.createMatching(matchingRequestCreate1,authUser1);

            // then - 1번 호출됐는지와, 예상 데이터 실제 데이터가 일치하는지 검증
            Mockito.verify(matchingRepository,Mockito.times(1)).save(Mockito.argThat(matching ->
                    matching.getUser().getId().equals(authUser1.getUserId()) &&
                            matching.getOutsourcing().getId().equals(matchingRequestCreate1.outsourcingId()) &&
                            matching.getPortfolio().getId().equals(matchingRequestCreate1.portfolioId())
            ));
        }
    }

    @Nested
    public class 매칭_수정_테스트 {
        @Test
        void updateMatching() {
            // given
            given(matchingRepository.findById(matchingId1)).willReturn(Optional.of(matching1));

            // when
            matchingService.updateMatching(matchingId1,matchingRequestUpdate1,authUser1);

            // then - 1번 호출됐는지와, 예상 데이터 실제 데이터가 일치하는지 검증
            Mockito.verify(matchingRepository,Mockito.times(1)).save(Mockito.argThat(matching ->
                    matching.getUser().getId().equals(matching1.getId()) &&
                            matching.getOutsourcing().getId().equals(matching1.getOutsourcing().getId()) &&
                            matching.getPortfolio().getId().equals(matching1.getPortfolio().getId()) &&
                            !matching.getStatus().equals(matching1.getStatus())
            ));
        }
    }

    @Nested
    public class 매칭_삭제_테스트 {
        @Test
        void deleteMatching() {
            // given
            given(matchingRepository.findById(matchingId1)).willReturn(Optional.of(matching1));

            // when
            matchingService.deleteMatching(matchingId1,authUser1);

            // then - 1번 호출됐는지와, 예상 데이터 실제 데이터가 일치하는지 검증
            Mockito.verify(matchingRepository,Mockito.times(1)).delete(Mockito.argThat(matching ->
                    matching.getUser().getId().equals(authUser1.getUserId()) &&
                            matching.getOutsourcing().getId().equals(matching1.getOutsourcing().getId()) &&
                            matching.getPortfolio().getId().equals(matching1.getPortfolio().getId())
            ));
        }
    }

    @Nested
    public class 매칭_다건_조회_테스트 {
        @Test
        @DisplayName("매칭 다건 조회 _ 외주 매칭 건")
        void test1() {
            // given
            List<Matching> matchings = List.of(matching1, matching2);
            Page<Matching> matchingPages = new PageImpl<>(matchings,pageable,matchings.size());
            given(matchingRepository.findAllByUserAndStatus(any(),any(),any())).willReturn(matchingPages);

            // when
            ApiResponse<Page<MatchingResponse>> actual = matchingService.getAllMatching(authUser1,pageable,status1);
            List<MatchingResponse> actualDatas = actual.getData().getContent();
            List<MatchingResponse.MatchingResponseGet> actualDataConvert = actualDatas.stream()
                    .map(actualData -> (MatchingResponse.MatchingResponseGet) actualData)
                    .toList();
            // then - 예상한 데이터와, 실제 데이터의 값이 일치하는지 검증
            assertEquals( actualDatas.size(), matchings.size() );
            assertEquals(matching1.getId(), actualDataConvert.get(0).id());
            assertEquals(matching1.getUser().getId(), actualDataConvert.get(0).userId());
            assertEquals(matching1.getPortfolio().getId(), actualDataConvert.get(0).outsourcingId());
            assertEquals(matching1.getOutsourcing().getId(), actualDataConvert.get(0).portfolioId());
            assertEquals(matching2.getId(), actualDataConvert.get(1).id());
            assertEquals(matching2.getUser().getId(), actualDataConvert.get(1).userId());
            assertEquals(matching2.getPortfolio().getId(), actualDataConvert.get(1).outsourcingId());
            assertEquals(matching2.getOutsourcing().getId(), actualDataConvert.get(1).portfolioId());
        }

        @Test
        @DisplayName("매칭 다건 조회 _ 모든 매칭")
        void test2() {
            // given
            List<Matching> matchings = List.of(matching1, matching2);
            Page<Matching> matchingPages = new PageImpl<>(matchings,pageable,matchings.size());
            given(matchingRepository.findAllByUser(any(),any())).willReturn(matchingPages);

            // when
            ApiResponse<Page<MatchingResponse>> actual = matchingService.getAllMatching(authUser1,pageable,null);
            List<MatchingResponse> actualDatas = actual.getData().getContent();
            List<MatchingResponse.MatchingResponseGet> actualDataConvert = actualDatas.stream()
                    .map(actualData -> (MatchingResponse.MatchingResponseGet) actualData)
                    .toList();
            // then - 예상한 데이터와, 실제 데이터의 값이 일치하는지 검증
            assertEquals( actualDatas.size(), matchings.size() );
            assertEquals(matching1.getId(), actualDataConvert.get(0).id());
            assertEquals(matching1.getUser().getId(), actualDataConvert.get(0).userId());
            assertEquals(matching1.getPortfolio().getId(), actualDataConvert.get(0).outsourcingId());
            assertEquals(matching1.getOutsourcing().getId(), actualDataConvert.get(0).portfolioId());
            assertEquals(matching2.getId(), actualDataConvert.get(1).id());
            assertEquals(matching2.getUser().getId(), actualDataConvert.get(1).userId());
            assertEquals(matching2.getPortfolio().getId(), actualDataConvert.get(1).outsourcingId());
            assertEquals(matching2.getOutsourcing().getId(), actualDataConvert.get(1).portfolioId());
        }
    }

}
package com.sparta.doguin.domain.portfolio.service;

import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.portfolio.entity.Portfolio;
import com.sparta.doguin.domain.portfolio.model.PortfolioRequest;
import com.sparta.doguin.domain.portfolio.model.PortfolioResponse;
import com.sparta.doguin.domain.portfolio.repository.PortfolioRepository;
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
class PortfolioServiceImplTest {

    @Mock
    PortfolioRepository portfolioRepository;

    @InjectMocks
    PortfolioServiceImpl portfolioService;

    Pageable pageable;
    User user1;
    User user2;
    AuthUser authUser1;
    AuthUser authUser2;
    Long portfolioId1;
    Long portfolioId2;
    Portfolio portfolio1;
    Portfolio portfolio2;
    PortfolioRequest.PortfolioRequestCreate portfolioRequestCreate1;
    PortfolioRequest.PortfolioRequestCreate portfolioRequestCreate2;
    PortfolioRequest.PortfolioRequestUpdate portfolioRequestUpdate1;
    PortfolioRequest.PortfolioRequestUpdate portfolioRequestUpdate2;
    AreaType area;


    @BeforeEach
    void setUp() {
        pageable = DataUtil.pageable();
        portfolioId1 = DataUtil.one();
        portfolioId2 = DataUtil.two();
        user1 = DataUtil.user1();
        user2 = DataUtil.user2();
        authUser1 = DataUtil.authUser1();
        authUser2 = DataUtil.authUser2();
        portfolio1 = DataUtil.portfolio1();
        portfolio2 = DataUtil.portfolio2();
        portfolioRequestCreate1 = DataUtil.portfolioRequestCreate1();
        portfolioRequestCreate2 = DataUtil.portfolioRequestCreate2();
        portfolioRequestUpdate1 = DataUtil.portfolioRequestUpdate1();
        portfolioRequestUpdate2 = DataUtil.portfolioRequestUpdate2();
        area = AreaType.SEOUL;
    }

    @Nested
    public class 포트폴리오_단일_조회_테스트 {
        @Test
        @DisplayName("포트폴리오 단일 조회 성공")
        void test() {
            // given
            given(portfolioRepository.findById(portfolioId1)).willReturn(Optional.of(portfolio1));

            // when
            ApiResponse<PortfolioResponse> actual = portfolioService.getPortfolio(portfolioId1);
            PortfolioResponse.PortfolioResponseGet actualData = (PortfolioResponse.PortfolioResponseGet) actual.getData();

            // then - 예상한 데이터와 실제 데이터가 동일한지 확인
            assertEquals(portfolio1.getUser().getId(),actualData.user_id());
            assertEquals(portfolio1.getTitle(),actualData.title());
            assertEquals(portfolio1.getContent(),actualData.content());

        }
    }
    @Nested
    public class 포트폴리오_생성_테스트 {
        @Test
        void test() {
            // given

            // when
            portfolioService.createPortfolio(portfolioRequestCreate1,authUser1,null);

            // then - 저장 한번 호출됐는지와, 예상 데이터와 실제 데이터가 일치 하는지 확인
            Mockito.verify(portfolioRepository,Mockito.times(1)).save(Mockito.argThat( portfolio ->
                    portfolio.getUser().getId().equals(authUser1.getUserId()) &&
                            portfolio.getTitle().equals(portfolioRequestCreate1.title()) &&
                            portfolio.getContent().equals(portfolioRequestCreate1.content())
            ));
        }
    }

    @Nested
    public class 포트폴리오_수정_테스트 {
        @Test
        void test() {
            // given
            given(portfolioRepository.findById(portfolioId1)).willReturn(Optional.of(portfolio1));

            // when
            portfolioService.updatePortfolio(portfolioId1, portfolioRequestUpdate1,authUser1,null);

            // then - 저장(수정) 한번 호출됐는지와, 예상 데이터와 실제 데이터가 일치 하는지 확인
            Mockito.verify(portfolioRepository,Mockito.times(1)).save(Mockito.argThat( portfolio ->
                    portfolio.getUser().getId().equals(authUser1.getUserId()) &&
                            !portfolio1.getTitle().equals(portfolioRequestUpdate1.title()) &&
                            !portfolio1.getContent().equals(portfolioRequestUpdate1.content())
            ));
        }
    }

    @Nested
    public class 포트폴리오_삭제_테스트 {
        @Test
        void test() {
            // given
            given(portfolioRepository.findById(portfolioId1)).willReturn(Optional.of(portfolio1));

            // when
            portfolioService.deletePortfolio(portfolioId1,authUser1,null);

            // then - 저장(수정) 한번 호출됐는지와, 예상 데이터와 실제 데이터가 일치 하는지 확인
            Mockito.verify(portfolioRepository,Mockito.times(1)).delete(Mockito.argThat( portfolio ->
                    portfolio.getUser().getId().equals(authUser1.getUserId()) &&
                            portfolio.getTitle().equals(portfolio1.getTitle()) &&
                            portfolio.getContent().equals(portfolio1.getContent())
            ));
        }
    }

    @Nested
    public class 포트폴리오_자신_다건_조회_테스트 {
        @Test
        @DisplayName("자신의 포트폴리오 모두 조회 성공 _ 지역 고름")
        void test1() {
            // given
            List<Portfolio> portfolios = List.of(portfolio1,portfolio2);
            Page<Portfolio> portfolioPages = new PageImpl<>(portfolios,pageable,portfolios.size());
            given(portfolioRepository.findAllByUserAndArea(any(),any(),any())).willReturn(portfolioPages);

            // when
            ApiResponse<Page<PortfolioResponse>> actual = portfolioService.getAllMyPortfolio(pageable,area,authUser1);
            List<PortfolioResponse> actualDatas = actual.getData().getContent();
            List<PortfolioResponse.PortfolioResponseGet> actualDataConvert = actualDatas.stream()
                    .map(actualData -> (PortfolioResponse.PortfolioResponseGet) actualData)
                    .toList();
            // then - 예상한 데이터와, 실제 데이터의 값이 일치하는지 검증
            assertEquals( actualDatas.size(), portfolios.size() );
            assertEquals(portfolio1.getId(), actualDataConvert.get(0).id());
            assertEquals(portfolio1.getUser().getId(), actualDataConvert.get(0).user_id());
            assertEquals(portfolio1.getTitle(), actualDataConvert.get(0).title());
            assertEquals(portfolio1.getContent(), actualDataConvert.get(0).content());
            assertEquals(portfolio2.getId(), actualDataConvert.get(1).id());
            assertEquals(portfolio2.getUser().getId(), actualDataConvert.get(1).user_id());
            assertEquals(portfolio2.getTitle(), actualDataConvert.get(1).title());
            assertEquals(portfolio2.getContent(), actualDataConvert.get(1).content());
        }

        @Test
        @DisplayName("자신의 포트폴리오 모두 조회 성공 _ 지역 고르지 않음")
        void test2() {
            // given
            List<Portfolio> portfolios = List.of(portfolio1,portfolio2);
            Page<Portfolio> portfolioPages = new PageImpl<>(portfolios,pageable,portfolios.size());
            given(portfolioRepository.findAllByUser(any(),any())).willReturn(portfolioPages);

            // when
            ApiResponse<Page<PortfolioResponse>> actual = portfolioService.getAllMyPortfolio(pageable,null,authUser1);
            List<PortfolioResponse> actualDatas = actual.getData().getContent();
            List<PortfolioResponse.PortfolioResponseGet> actualDataConvert = actualDatas.stream()
                    .map(actualData -> (PortfolioResponse.PortfolioResponseGet) actualData)
                    .toList();
            // then - 예상한 데이터와, 실제 데이터의 값이 일치하는지 검증
            assertEquals( actualDatas.size(), portfolios.size() );
            assertEquals(portfolio1.getId(), actualDataConvert.get(0).id());
            assertEquals(portfolio1.getUser().getId(), actualDataConvert.get(0).user_id());
            assertEquals(portfolio1.getTitle(), actualDataConvert.get(0).title());
            assertEquals(portfolio1.getContent(), actualDataConvert.get(0).content());
            assertEquals(portfolio2.getId(), actualDataConvert.get(1).id());
            assertEquals(portfolio2.getUser().getId(), actualDataConvert.get(1).user_id());
            assertEquals(portfolio2.getTitle(), actualDataConvert.get(1).title());
            assertEquals(portfolio2.getContent(), actualDataConvert.get(1).content());
        }
    }

    @Nested
    public class 포트폴리오_모두_다건_조회_테스트 {
        @Test
        @DisplayName("모두의 포트폴리오 조회 성공 _ 지역 고름")
        void test1() {
            // given
            List<Portfolio> portfolios = List.of(portfolio1,portfolio2);
            Page<Portfolio> portfolioPages = new PageImpl<>(portfolios,pageable,portfolios.size());
            given(portfolioRepository.findAllByArea(any(),any())).willReturn(portfolioPages);

            // when
            ApiResponse<Page<PortfolioResponse>> actual = portfolioService.getAllOtherPortfolio(pageable,area);
            List<PortfolioResponse> actualDatas = actual.getData().getContent();
            List<PortfolioResponse.PortfolioResponseGet> actualDataConvert = actualDatas.stream()
                    .map(actualData -> (PortfolioResponse.PortfolioResponseGet) actualData)
                    .toList();
            // then - 예상한 데이터와, 실제 데이터의 값이 일치하는지 검증
            assertEquals( actualDatas.size(), portfolios.size() );
            assertEquals(portfolio1.getId(), actualDataConvert.get(0).id());
            assertEquals(portfolio1.getUser().getId(), actualDataConvert.get(0).user_id());
            assertEquals(portfolio1.getTitle(), actualDataConvert.get(0).title());
            assertEquals(portfolio1.getContent(), actualDataConvert.get(0).content());
            assertEquals(portfolio2.getId(), actualDataConvert.get(1).id());
            assertEquals(portfolio2.getUser().getId(), actualDataConvert.get(1).user_id());
            assertEquals(portfolio2.getTitle(), actualDataConvert.get(1).title());
            assertEquals(portfolio2.getContent(), actualDataConvert.get(1).content());
        }

        @Test
        @DisplayName("모두의 포트폴리오 조회 성공 _ 지역 고르지 않음")
        void test2() {
            // given
            List<Portfolio> portfolios = List.of(portfolio1,portfolio2);
            Page<Portfolio> portfolioPages = new PageImpl<>(portfolios,pageable,portfolios.size());
            given(portfolioRepository.findAllByArea(any(),any())).willReturn(portfolioPages);

            // when
            ApiResponse<Page<PortfolioResponse>> actual = portfolioService.getAllOtherPortfolio(pageable,area);
            List<PortfolioResponse> actualDatas = actual.getData().getContent();
            List<PortfolioResponse.PortfolioResponseGet> actualDataConvert = actualDatas.stream()
                    .map(actualData -> (PortfolioResponse.PortfolioResponseGet) actualData)
                    .toList();
            // then - 예상한 데이터와, 실제 데이터의 값이 일치하는지 검증
            assertEquals( actualDatas.size(), portfolios.size() );
            assertEquals(portfolio1.getId(), actualDataConvert.get(0).id());
            assertEquals(portfolio1.getUser().getId(), actualDataConvert.get(0).user_id());
            assertEquals(portfolio1.getTitle(), actualDataConvert.get(0).title());
            assertEquals(portfolio1.getContent(), actualDataConvert.get(0).content());
            assertEquals(portfolio2.getId(), actualDataConvert.get(1).id());
            assertEquals(portfolio2.getUser().getId(), actualDataConvert.get(1).user_id());
            assertEquals(portfolio2.getTitle(), actualDataConvert.get(1).title());
            assertEquals(portfolio2.getContent(), actualDataConvert.get(1).content());
        }
    }



}
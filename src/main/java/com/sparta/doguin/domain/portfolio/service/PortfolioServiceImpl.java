package com.sparta.doguin.domain.portfolio.service;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.common.exception.PortfolioException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.portfolio.entity.Portfolio;
import com.sparta.doguin.domain.portfolio.model.PortfolioDto;
import com.sparta.doguin.domain.portfolio.repository.PortfolioRepository;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.doguin.domain.common.response.ApiResponsePortfolioEnum.PORTFOLIO_NOT_FOUND;
import static com.sparta.doguin.domain.common.response.ApiResponsePortfolioEnum.PORTFOLIO_OK;


@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    /**
     * ID로 특정 포트폴리오 찾는 메서드
     *
     * @param portfolioId / 조회할 포트폴리오 ID
     * @return ApiResponse<PortfolioDto> / 포트폴리오 데이터 반환
     * @since 1.0
     * @author 김경민
     */
    @Transactional(readOnly = true)
    @Override
    public ApiResponse<PortfolioDto> getPortfolio(Long portfolioId) {
        Portfolio portfolio = findById(portfolioId);
        PortfolioDto portfolioResponse = PortfolioDto.PortfolioResponse.of(portfolio);
        return ApiResponse.of(PORTFOLIO_OK,portfolioResponse);
    }

    /**
     * 포트폴리오 생성 메서드
     *
     * @param portfolioRequest / 생성할 포트폴리오 데이터
     * @param authUser / 포트폴리오 생성할 유저
     * @return ApiResponse<Void> / 성공 응답 반환
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    @Override
    public ApiResponse<Void> createPortfolio(PortfolioDto.PortfolioRequest portfolioRequest, AuthUser authUser) {
        User user = userRepository.findById(Long.parseLong(authUser.getUserId())).orElseThrow();
        Portfolio portfolio = Portfolio.builder()
                .user(user)
                .title(portfolioRequest.title())
                .content(portfolioRequest.content())
                .work_experience(portfolioRequest.work_experience())
                .work_type(portfolioRequest.work_type())
                .project_history(portfolioRequest.proejct_history())
                .area(portfolioRequest.area())
                .build();
        portfolioRepository.save(portfolio);
        return ApiResponse.of(PORTFOLIO_OK);
    }

    /**
     * 포트폴리오 수정 메서드
     *
     * @param portfolioId / 수정할 포트폴리오 ID
     * @param portfolioRequestUpdate / 수정할 포트폴리오 데이터
     * @return ApiResponse<Void> / 성공 응답 반환
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    @Override
    public ApiResponse<Void> updatePortfolio(Long portfolioId, PortfolioDto.PortfolioRequestUpdate portfolioRequestUpdate) {
        System.out.println(portfolioRequestUpdate.content());
        Portfolio findPortfolio = findById(portfolioId);
        Portfolio portfolio = Portfolio.builder()
                .id(findPortfolio.getId())
                .user(findPortfolio.getUser())
                .title(portfolioRequestUpdate.title())
                .content(portfolioRequestUpdate.content())
                .work_experience(portfolioRequestUpdate.work_experience())
                .work_type(portfolioRequestUpdate.work_type())
                .project_history(portfolioRequestUpdate.proejct_history())
                .area(portfolioRequestUpdate.area())
                .build();
        portfolioRepository.save(portfolio);
        return ApiResponse.of(PORTFOLIO_OK);
    }

    /**
     * 포트폴리오 삭제 메서드
     *
     * @param portfolioId / 삭제할 포트폴리오 ID
     * @return ApiResponse<Void> / 성공 응답 반환
     * @since 1.0
     * @author 김경민
     */
    @Transactional(readOnly = true)
    @Override
    public ApiResponse<Void> deletePortfolio(Long portfolioId) {
        Portfolio getPortfolio = findById(portfolioId);
        portfolioRepository.delete(getPortfolio);
        return ApiResponse.of(PORTFOLIO_OK);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse<Page<PortfolioDto>> getAllMyPortfolio(Pageable pageable, AreaType area, AuthUser authUser) {
        User user = userRepository.findById(Long.parseLong(authUser.getUserId())).orElseThrow();
        Page<Portfolio> pageablePortfolio;
        if (area == null) {
            pageablePortfolio = portfolioRepository.findAllByUser(user,pageable);
        } else {
            pageablePortfolio = portfolioRepository.findAllByUserAndArea(user,pageable,area);
        }

        Page<PortfolioDto> portfolios = pageablePortfolio.map(PortfolioDto.PortfolioResponse::of);
        return ApiResponse.of(PORTFOLIO_OK,portfolios);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse<Page<PortfolioDto>> getAllOtherPortfolio(Pageable pageable, AreaType area) {
        Page<Portfolio> pageablePortfolio;
        if (area == null) {
            pageablePortfolio = portfolioRepository.findAllBy(pageable);
        } else {
            pageablePortfolio = portfolioRepository.findAllByArea(pageable,area);
        }

        Page<PortfolioDto> portfolios = pageablePortfolio.map(PortfolioDto.PortfolioResponse::of);
        return ApiResponse.of(PORTFOLIO_OK,portfolios);
    }

    /**
     * ID로 포트폴리오 찾는 메서드
     *
     * @param portfolioId / 찾을 포트폴리오 ID
     * @return Portfoli / 찾은 포트폴리오 데이터 반환
     * @throws PortfolioException / 포트폴리오 조회 실패시 발생되는 예외
     * @since 1.0
     * @author 김경민
     */
    @Transactional(readOnly = true)
    public Portfolio findById(Long portfolioId) {
        return portfolioRepository.findById(portfolioId).orElseThrow(() -> new PortfolioException(PORTFOLIO_NOT_FOUND));
    }
}

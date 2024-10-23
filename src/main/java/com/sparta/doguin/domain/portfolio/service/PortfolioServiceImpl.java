package com.sparta.doguin.domain.portfolio.service;

import com.sparta.doguin.domain.common.exception.PortfolioException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponsePortfolioEnum;
import com.sparta.doguin.domain.portfolio.entity.Portfolio;
import com.sparta.doguin.domain.portfolio.model.PortfolioDto;
import com.sparta.doguin.domain.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.sparta.doguin.domain.common.response.ApiResponsePortfolioEnum.PORTFOLIO_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {
    private final PortfolioRepository portfolioRepository;

    @Override
    public ApiResponse<PortfolioDto> getPortfolio(Long portfolioId) {
        Portfolio portfolio = findById(portfolioId);
        PortfolioDto portfolioResponse = PortfolioDto.PortfolioResponse.of(portfolio);
        ApiResponsePortfolioEnum apiResponsePortfolio = ApiResponsePortfolioEnum.PORTFOLIO_OK;
        return ApiResponse.of(apiResponsePortfolio,portfolioResponse);
    }

    // TODO: 유저 추가해야함
    @Override
    public ApiResponse<Void> createPortfolio(PortfolioDto.PortfolioRequest portfolioRequest) {
        Portfolio portfolio = Portfolio.builder()
                .title(portfolioRequest.title())
                .content(portfolioRequest.content())
                .work_experience(portfolioRequest.work_experience())
                .work_type(portfolioRequest.work_type())
                .project_history(portfolioRequest.proejct_history())
                .area(portfolioRequest.area())
                .build();
        portfolioRepository.save(portfolio);
        ApiResponsePortfolioEnum apiResponsePortfolio = ApiResponsePortfolioEnum.PORTFOLIO_OK;
        return ApiResponse.of(apiResponsePortfolio);
    }

    // TODO: 유저 추가해야함
    @Override
    public ApiResponse<Void> updatePortfolio(Long portfolioId, PortfolioDto.PortfolioRequest portfolioRequest) {
        Portfolio getPortfolio = findById(portfolioId);
        Portfolio portfolio = Portfolio.builder()
                .id(getPortfolio.getId())
                .title(portfolioRequest.title())
                .content(portfolioRequest.content())
                .work_experience(portfolioRequest.work_experience())
                .work_type(portfolioRequest.work_type())
                .project_history(portfolioRequest.proejct_history())
                .area(portfolioRequest.area())
                .build();
        portfolioRepository.save(portfolio);
        ApiResponsePortfolioEnum apiResponsePortfolio = ApiResponsePortfolioEnum.PORTFOLIO_OK;
        return ApiResponse.of(apiResponsePortfolio);
    }

    @Override
    public ApiResponse<Void> deletePortfolio(Long portfolioId) {
        Portfolio getPortfolio = findById(portfolioId);
        portfolioRepository.delete(getPortfolio);
        ApiResponsePortfolioEnum apiResponsePortfolio = ApiResponsePortfolioEnum.PORTFOLIO_OK;
        return ApiResponse.of(apiResponsePortfolio);
    }

    public Portfolio findById(Long portfolioId) {
        return portfolioRepository.findById(portfolioId).orElseThrow(() -> new PortfolioException(PORTFOLIO_NOT_FOUND));
    }
}

package com.sparta.doguin.domain.portfolio.service;

import com.sparta.doguin.domain.common.exception.PortfolioException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponsePortfolio;
import com.sparta.doguin.domain.portfolio.entity.Portfolio;
import com.sparta.doguin.domain.portfolio.model.PortfolioRequestDto;
import com.sparta.doguin.domain.portfolio.model.PortfolioResponse;
import com.sparta.doguin.domain.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.sparta.doguin.domain.common.response.ApiResponsePortfolio.PORTFOLIO_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {
    private final PortfolioRepository portfolioRepository;

    @Override
    public ApiResponse<PortfolioResponse> getPortfolio(Long portfolioId) {
        Portfolio portfolio = findById(portfolioId);
//        PortfolioResponse portfolioResponse = new PortfolioResponseDto(portfolio);
        ApiResponsePortfolio apiResponsePortfolio = ApiResponsePortfolio.PORTFOLIO_OK;
//        return ApiResponse.of(apiResponsePortfolio,portfolioResponse);
        return null;
    }

    @Override
    public ApiResponse<Void> createPortfolio(PortfolioRequestDto portfolioRequest) {
        Portfolio portfolio = Portfolio.builder()
                .title(portfolioRequest.getTitle())
                .content(portfolioRequest.getContent())
                .work_experience(portfolioRequest.getWork_experience())
                .work_type(portfolioRequest.getWork_type())
                .proejct_history(portfolioRequest.getProejct_history())
                .area(portfolioRequest.getArea())
                .build();
        portfolioRepository.save(portfolio);
        ApiResponsePortfolio apiResponsePortfolio = ApiResponsePortfolio.PORTFOLIO_OK;
        return ApiResponse.of(apiResponsePortfolio);
    }

    @Override
    public ApiResponse<Void> updatePortfolio(Long portfolioId, PortfolioRequestDto portfolioRequest) {
        Portfolio getPortfolio = findById(portfolioId);
        Portfolio portfolio = Portfolio.builder()
                .id(getPortfolio.getId())
                .title(portfolioRequest.getTitle())
                .content(portfolioRequest.getContent())
                .work_experience(portfolioRequest.getWork_experience())
                .work_type(portfolioRequest.getWork_type())
                .proejct_history(portfolioRequest.getProejct_history())
                .area(portfolioRequest.getArea())
                .build();
        portfolioRepository.save(portfolio);
        ApiResponsePortfolio apiResponsePortfolio = ApiResponsePortfolio.PORTFOLIO_OK;
        return ApiResponse.of(apiResponsePortfolio);
    }

    @Override
    public ApiResponse<Void> deletePortfolio(Long portfolioId) {
        Portfolio getPortfolio = findById(portfolioId);
        portfolioRepository.delete(getPortfolio);
        ApiResponsePortfolio apiResponsePortfolio = ApiResponsePortfolio.PORTFOLIO_OK;
        return ApiResponse.of(apiResponsePortfolio);
    }

    public Portfolio findById(Long portfolioId) {
        return portfolioRepository.findById(portfolioId).orElseThrow(() -> new PortfolioException(PORTFOLIO_NOT_FOUND));
    }
}

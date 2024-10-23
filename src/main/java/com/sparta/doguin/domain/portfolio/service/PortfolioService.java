package com.sparta.doguin.domain.portfolio.service;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.portfolio.model.PortfolioDto;

public interface PortfolioService {
    ApiResponse<PortfolioDto> getPortfolio(Long portfolioId);
    ApiResponse<Void> createPortfolio(PortfolioDto.PortfolioRequest portfolioRequest);
    ApiResponse<Void> updatePortfolio(Long portfolioId, PortfolioDto.PortfolioRequest portfolioRequest);
    ApiResponse<Void> deletePortfolio(Long portfolioId);
}

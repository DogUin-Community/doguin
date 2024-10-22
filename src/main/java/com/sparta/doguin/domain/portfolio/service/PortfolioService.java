package com.sparta.doguin.domain.portfolio.service;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.portfolio.model.PortfolioRequestDto;
import com.sparta.doguin.domain.portfolio.model.PortfolioResponse;

public interface PortfolioService {
    ApiResponse<PortfolioResponse> getPortfolio(Long portfolioId);
    ApiResponse<Void> createPortfolio(PortfolioRequestDto portfolioRequest);
    ApiResponse<Void> updatePortfolio(Long portfolioId, PortfolioRequestDto portfolioRequest);
    ApiResponse<Void> deletePortfolio(Long portfolioId);
}

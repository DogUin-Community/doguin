package com.sparta.doguin.domain.portfolio.service;

import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.portfolio.model.PortfolioRequest;
import com.sparta.doguin.domain.portfolio.model.PortfolioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PortfolioService {
    ApiResponse<PortfolioResponse> getPortfolio(Long portfolioId);
    ApiResponse<Void> createPortfolio(PortfolioRequest.PortfolioRequestCreate portfolioRequest, AuthUser authUser);
    ApiResponse<Void> updatePortfolio(Long portfolioId, PortfolioRequest.PortfolioRequestUpdate portfolioRequestUpdate, AuthUser authUser);
    ApiResponse<Void> deletePortfolio(Long portfolioId,AuthUser authUser);
    ApiResponse<Page<PortfolioResponse>> getAllMyPortfolio(Pageable pageable, AreaType area,AuthUser authUser);
    ApiResponse<Page<PortfolioResponse>> getAllOtherPortfolio(Pageable pageable, AreaType area);
}

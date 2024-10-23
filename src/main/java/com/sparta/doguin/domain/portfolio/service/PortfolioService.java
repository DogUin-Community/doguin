package com.sparta.doguin.domain.portfolio.service;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.portfolio.model.PortfolioDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PortfolioService {
    ApiResponse<PortfolioDto> getPortfolio(Long portfolioId);
    ApiResponse<Void> createPortfolio(PortfolioDto.PortfolioRequest portfolioRequest, AuthUser authUser);
    ApiResponse<Void> updatePortfolio(Long portfolioId, PortfolioDto.PortfolioRequestUpdate portfolioRequestUpdate,AuthUser authUser);
    ApiResponse<Void> deletePortfolio(Long portfolioId,AuthUser authUser);
    ApiResponse<Page<PortfolioDto>> getAllMyPortfolio(Pageable pageable, AreaType area,AuthUser authUser);
    ApiResponse<Page<PortfolioDto>> getAllOtherPortfolio(Pageable pageable, AreaType area);
}

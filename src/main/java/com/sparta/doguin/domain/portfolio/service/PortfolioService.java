package com.sparta.doguin.domain.portfolio.service;

import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.portfolio.model.PortfolioRequest;
import com.sparta.doguin.domain.portfolio.model.PortfolioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PortfolioService {
    PortfolioResponse getPortfolio(Long portfolioId);
    ApiResponse<PortfolioResponse> createPortfolio(PortfolioRequest.PortfolioRequestCreate portfolioRequest, AuthUser authUser, List<MultipartFile> files);
    ApiResponse<Void> updatePortfolio(Long portfolioId, PortfolioRequest.PortfolioRequestUpdate portfolioRequestUpdate, AuthUser authUser, List<MultipartFile> files);
    ApiResponse<Void> deletePortfolio(Long portfolioId,AuthUser authUser);
    Page<PortfolioResponse> getAllMyPortfolio(Pageable pageable, AreaType area,AuthUser authUser);
    Page<PortfolioResponse> getAllOtherPortfolio(Pageable pageable, AreaType area);
}

package com.sparta.doguin.domain.portfolio.controller;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.portfolio.model.PortfolioRequestDto;
import com.sparta.doguin.domain.portfolio.model.PortfolioResponse;
import com.sparta.doguin.domain.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolio")
public class PortfolioController {
    private final PortfolioService portfolioService;

    @GetMapping("/{portfolioId}")
    ResponseEntity<ApiResponse<PortfolioResponse>> getPortfolio(@PathVariable Long portfolioId) {
        ApiResponse<PortfolioResponse> apiResponse = portfolioService.getPortfolio(portfolioId);
        return ApiResponse.of(apiResponse);
    }

    @PostMapping
    ResponseEntity<ApiResponse<Void>> getPortfolio(PortfolioRequestDto portfolioRequestDto) {
        ApiResponse<Void> apiResponse = portfolioService.createPortfolio(portfolioRequestDto);
        return ApiResponse.of(apiResponse);
    }

    @PutMapping("/{portfolioId}")
    ResponseEntity<ApiResponse<Void>> updatePortfolio(@PathVariable Long portfolioId, PortfolioRequestDto portfolioRequestDto) {
        ApiResponse<Void> apiResponse = portfolioService.updatePortfolio(portfolioId,portfolioRequestDto);
        return ApiResponse.of(apiResponse);
    }

    @DeleteMapping("/{portfolioId}")
    ResponseEntity<ApiResponse<Void>> deletePortfolio(@PathVariable Long portfolioId) {
        ApiResponse<Void> apiResponse = portfolioService.deletePortfolio(portfolioId);
        return ApiResponse.of(apiResponse);
    }

}

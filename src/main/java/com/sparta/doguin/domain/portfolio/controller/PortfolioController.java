package com.sparta.doguin.domain.portfolio.controller;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.portfolio.model.PortfolioDto;
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
    ResponseEntity<ApiResponse<PortfolioDto>> getPortfolio(@PathVariable Long portfolioId) {
        ApiResponse<PortfolioDto> apiResponse = portfolioService.getPortfolio(portfolioId);
        return ApiResponse.of(apiResponse);
    }

    @PostMapping
    ResponseEntity<ApiResponse<Void>> getPortfolio(@RequestBody PortfolioDto.PortfolioRequest portfolioRequest) {
        ApiResponse<Void> apiResponse = portfolioService.createPortfolio(portfolioRequest);
        return ApiResponse.of(apiResponse);
    }

    @PutMapping("/{portfolioId}")
    ResponseEntity<ApiResponse<Void>> updatePortfolio(@PathVariable Long portfolioId, PortfolioDto.PortfolioRequest portfolioRequest) {
        ApiResponse<Void> apiResponse = portfolioService.updatePortfolio(portfolioId,portfolioRequest);
        return ApiResponse.of(apiResponse);
    }

    @DeleteMapping("/{portfolioId}")
    ResponseEntity<ApiResponse<Void>> deletePortfolio(@PathVariable Long portfolioId) {
        ApiResponse<Void> apiResponse = portfolioService.deletePortfolio(portfolioId);
        return ApiResponse.of(apiResponse);
    }

}

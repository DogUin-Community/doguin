package com.sparta.doguin.domain.portfolio.controller;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.portfolio.model.PortfolioDto;
import com.sparta.doguin.domain.portfolio.service.PortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    ResponseEntity<ApiResponse<Void>> createPortfolio(@Valid @RequestBody PortfolioDto.PortfolioRequest portfolioRequest, @AuthenticationPrincipal AuthUser authUser) {
        ApiResponse<Void> apiResponse = portfolioService.createPortfolio(portfolioRequest, authUser);
        return ApiResponse.of(apiResponse);
    }

    /**
     * 자신의 포트폴리오들 조회
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<PortfolioDto>>> getAllMyPortfolio(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "desc", required = false) String sort,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(required = false) AreaType area
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sort);
        Pageable pageable = PageRequest.of(page, size, direction,"createdAt");
        ApiResponse<Page<PortfolioDto>> apiResponse = portfolioService.getAllMyPortfolio(pageable,area,authUser);
        return ApiResponse.of(apiResponse);
    }

    /**
     * 자신과, 다른사람들의 포트폴리오 확인 가능
     */
    @GetMapping("/other")
    public ResponseEntity<ApiResponse<Page<PortfolioDto>>> getAllOtherPortfolio(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "desc", required = false) String sort,
            @RequestParam(required = false) AreaType area
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sort);
        Pageable pageable = PageRequest.of(page, size, direction,"createdAt");
        ApiResponse<Page<PortfolioDto>> apiResponse = portfolioService.getAllOtherPortfolio(pageable,area);
        return ApiResponse.of(apiResponse);
    }


    @PutMapping("/{portfolioId}")
    ResponseEntity<ApiResponse<Void>> updatePortfolio(@PathVariable Long portfolioId, @RequestBody PortfolioDto.PortfolioRequestUpdate portfolioRequestUpdate) {
        ApiResponse<Void> apiResponse = portfolioService.updatePortfolio(portfolioId,portfolioRequestUpdate);
        return ApiResponse.of(apiResponse);
    }

    @DeleteMapping("/{portfolioId}")
    ResponseEntity<ApiResponse<Void>> deletePortfolio(@PathVariable Long portfolioId) {
        ApiResponse<Void> apiResponse = portfolioService.deletePortfolio(portfolioId);
        return ApiResponse.of(apiResponse);
    }

}

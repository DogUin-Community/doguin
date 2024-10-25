package com.sparta.doguin.domain.portfolio.controller;

import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.portfolio.model.PortfolioRequest;
import com.sparta.doguin.domain.portfolio.model.PortfolioResponse;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    ResponseEntity<ApiResponse<PortfolioResponse>> createPortfolio(
            @Valid @RequestPart PortfolioRequest.PortfolioRequestCreate portfolioRequest,
            @RequestPart(required = false) List<MultipartFile> files,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        ApiResponse<PortfolioResponse> apiResponse = portfolioService.createPortfolio(portfolioRequest, authUser,files);
        return ApiResponse.of(apiResponse);
    }

    /**
     * 자신의 포트폴리오들 조회
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<PortfolioResponse>>> getAllMyPortfolio(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "desc", required = false) String sort,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(required = false) AreaType area
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sort);
        Pageable pageable = PageRequest.of(page, size, direction,"createdAt");
        ApiResponse<Page<PortfolioResponse>> apiResponse = portfolioService.getAllMyPortfolio(pageable,area,authUser);
        return ApiResponse.of(apiResponse);
    }

    /**
     * 자신과, 다른사람들의 포트폴리오 확인 가능
     */
    @GetMapping("/other")
    public ResponseEntity<ApiResponse<Page<PortfolioResponse>>> getAllOtherPortfolio(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "desc", required = false) String sort,
            @RequestParam(required = false) AreaType area
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sort);
        Pageable pageable = PageRequest.of(page, size, direction,"createdAt");
        ApiResponse<Page<PortfolioResponse>> apiResponse = portfolioService.getAllOtherPortfolio(pageable,area);
        return ApiResponse.of(apiResponse);
    }

    @PutMapping("/{portfolioId}")
    ResponseEntity<ApiResponse<Void>> updatePortfolio(
            @PathVariable Long portfolioId,
            @RequestPart PortfolioRequest.PortfolioRequestUpdate portfolioRequestUpdate,
            @RequestPart(required = false) List<MultipartFile> files,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        ApiResponse<Void> apiResponse = portfolioService.updatePortfolio(portfolioId,portfolioRequestUpdate,authUser,files);
        return ApiResponse.of(apiResponse);
    }

    @DeleteMapping("/{portfolioId}")
    ResponseEntity<ApiResponse<Void>> deletePortfolio(
            @PathVariable Long portfolioId,
            @RequestPart PortfolioRequest.PortfolioRequestDelete portfolioRequestDelete,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        ApiResponse<Void> apiResponse = portfolioService.deletePortfolio(portfolioId,authUser,portfolioRequestDelete);
        return ApiResponse.of(apiResponse);
    }

}

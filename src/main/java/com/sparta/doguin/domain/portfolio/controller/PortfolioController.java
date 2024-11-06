package com.sparta.doguin.domain.portfolio.controller;

import com.sparta.doguin.domain.common.response.ApiResponsePortfolioEnum;
import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.portfolio.model.PortfolioRequest;
import com.sparta.doguin.domain.portfolio.model.PortfolioResponse;
import com.sparta.doguin.domain.portfolio.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "포트폴리오 API",description = "포트폴리오 관련된 API를 확인 할 수 있습니다")
@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/portfolios")
public class PortfolioController {
    private final PortfolioService portfolioService;

    @Operation(summary = "특정 ID의 포트폴리오 단건 가져오기", description = "특정 ID 포트폴리오 단건 조회 API")
    @GetMapping("/{portfolioId}")
    ResponseEntity<ApiResponse<PortfolioResponse>> getPortfolio(@PathVariable Long portfolioId) {
        PortfolioResponse response = portfolioService.getPortfolio(portfolioId);
        ApiResponse<PortfolioResponse> apiResponse = ApiResponse.of(ApiResponsePortfolioEnum.PORTFOLIO_OK, response);
        return ApiResponse.of(apiResponse);
    }

    @Operation(summary = "포트폴리오 생성", description = "포트폴리오 생성 API")
    @PostMapping
    ResponseEntity<ApiResponse<PortfolioResponse>> createPortfolio(
            @Valid @RequestPart PortfolioRequest.PortfolioRequestCreate portfolioRequest,
            @RequestPart(required = false) List<MultipartFile> files,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        ApiResponse<PortfolioResponse> apiResponse = portfolioService.createPortfolio(portfolioRequest, authUser,files);
        return ApiResponse.of(apiResponse);
    }

    @Operation(summary = "포트폴리오 수정", description = "포트폴리오 수정 API")
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

    @Operation(summary = "포트폴리오 삭제", description = "포트폴리오 삭제 API")
    @DeleteMapping("/{portfolioId}")
    ResponseEntity<ApiResponse<Void>> deletePortfolio(
            @PathVariable Long portfolioId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        ApiResponse<Void> apiResponse = portfolioService.deletePortfolio(portfolioId,authUser);
        return ApiResponse.of(apiResponse);
    }

    /**
     * 자신의 포트폴리오들 조회
     */
    @Operation(summary = "자신의 모든 포트폴리오 가져오기", description = "자신의 포투폴리오 다건 조회 API")
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
        Page<PortfolioResponse> response = portfolioService.getAllMyPortfolio(pageable,area,authUser);
        ApiResponse<Page<PortfolioResponse>> apiResponse = ApiResponse.of(ApiResponsePortfolioEnum.PORTFOLIO_OK, response);
        return ApiResponse.of(apiResponse);
    }

    /**
     * 자신과, 다른사람들의 포트폴리오 확인 가능
     */
    @Operation(summary = "모든사람의 포트폴리오 가져오기", description = "모든사람 포투폴리오 다건 조회 API")
    @GetMapping("/other")
    public ResponseEntity<ApiResponse<Page<PortfolioResponse>>> getAllOtherPortfolio(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "desc", required = false) String sort,
            @RequestParam(required = false) AreaType area
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sort);
        Pageable pageable = PageRequest.of(page, size, direction,"createdAt");
        Page<PortfolioResponse> response = portfolioService.getAllOtherPortfolio(pageable,area);
        ApiResponse<Page<PortfolioResponse>> apiResponse = ApiResponse.of(ApiResponsePortfolioEnum.PORTFOLIO_OK, response);
        return ApiResponse.of(apiResponse);
    }

}

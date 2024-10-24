package com.sparta.doguin.domain.matching.controller;

import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.matching.constans.MathingStatusType;
import com.sparta.doguin.domain.matching.model.MatchingRequest;
import com.sparta.doguin.domain.matching.model.MatchingResponse;
import com.sparta.doguin.domain.matching.service.MatchingService;
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
@RequestMapping("/api/v1/matching")
public class MathchingController {
    private final MatchingService matchingService;
    /**
     * 자신의 매칭중인 목록 확인 가능 (상태별로도 가능)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<MatchingResponse>>> getAllMatching(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "desc", required = false) String sort,
            @RequestParam(required = false) MathingStatusType status,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sort);
        Pageable pageable = PageRequest.of(page, size, direction,"createdAt");
        ApiResponse<Page<MatchingResponse>> apiResponse = matchingService.getAllMatching(authUser,pageable,status);
        return ApiResponse.of(apiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createMatching(@RequestBody MatchingRequest.MatchingRequestCreate reqDto, @AuthenticationPrincipal AuthUser authUser){
        ApiResponse<Void> apiResponse = matchingService.createMatching(reqDto,authUser);
        return ApiResponse.of(apiResponse);
    }

    @PutMapping("/{matchingId}")
    public ResponseEntity<ApiResponse<Void>> updateMatching(@PathVariable Long matchingId, @RequestBody MatchingRequest.MatchingRequestUpdate reqDto, @AuthenticationPrincipal AuthUser authUser){
        ApiResponse<Void> apiResponse = matchingService.updateMatching(matchingId,reqDto,authUser);
        return ApiResponse.of(apiResponse);
    }

    @DeleteMapping("/{matchingId}")
    public ResponseEntity<ApiResponse<Void>> deleteMatching(@PathVariable Long matchingId,@AuthenticationPrincipal AuthUser authUser){
        ApiResponse<Void> apiResponse = matchingService.deleteMatching(matchingId,authUser);
        return ApiResponse.of(apiResponse);
    }

}

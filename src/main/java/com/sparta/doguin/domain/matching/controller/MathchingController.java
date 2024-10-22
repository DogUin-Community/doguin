package com.sparta.doguin.domain.matching.controller;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.matching.model.MatchingDto;
import com.sparta.doguin.domain.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/matching")
public class MathchingController {
    private final MatchingService matchingService;

    @GetMapping("/{matchingId}")
    public ResponseEntity<ApiResponse<MatchingDto>> getOutsourcing(@PathVariable Long matchingId) {
        ApiResponse<MatchingDto> apiResponse = matchingService.getMatching(matchingId);
        return ApiResponse.of(apiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createOutsourcing(@RequestBody MatchingDto.MatchingRequest reqDto,@AuthenticationPrincipal AuthUser authUser){
        ApiResponse<Void> apiResponse = matchingService.createMatching(reqDto,authUser);
        return ApiResponse.of(apiResponse);
    }

    @PutMapping("/{matchingId}")
    public ResponseEntity<ApiResponse<Void>> updateOutsourcing(@PathVariable Long matchingId, @RequestBody MatchingDto.MatchingRequestUpdate reqDto){
        ApiResponse<Void> apiResponse = matchingService.updateMatching(matchingId,reqDto);
        return ApiResponse.of(apiResponse);
    }

    @DeleteMapping("/{matchingId}")
    public ResponseEntity<ApiResponse<Void>> deleteOutsourcing(@PathVariable Long matchingId){
        ApiResponse<Void> apiResponse = matchingService.deleteMatching(matchingId);
        return ApiResponse.of(apiResponse);
    }

}

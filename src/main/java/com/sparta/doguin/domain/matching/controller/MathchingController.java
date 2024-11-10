package com.sparta.doguin.domain.matching.controller;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.matching.model.MatchingRequest;
import com.sparta.doguin.domain.matching.model.MatchingRequestSearch;
import com.sparta.doguin.domain.matching.model.MatchingResponse;
import com.sparta.doguin.domain.matching.service.MatchingService;
import com.sparta.doguin.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@Tag(name = "매칭 API",description = "매칭 관련된 API를 확인 할 수 있습니다")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/matchings")
public class MathchingController {
    private final MatchingService matchingService;
    /**
     * 자신의 매칭중인 목록 확인 가능 (상태별로도 가능)
     */
    @Operation(summary = "자신의 모든 매칭 가져오기", description = "자신의 매칭 다건 가져오기 API")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<MatchingResponse>>> getAllMatching(
            @ModelAttribute @Valid MatchingRequestSearch request,
            @AuthenticationPrincipal AuthUser authUser
    ){
        Sort.Direction direction = Sort.Direction.fromString(request.getSort());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), direction,"createdAt");
        ApiResponse<Page<MatchingResponse>> apiResponse = matchingService.getAllMatching(authUser,pageable,request.getStatus());
        return ApiResponse.of(apiResponse);
    }

    @Operation(summary = "매칭 생성", description = "매칭 생성 API")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createMatching(
            @Valid @RequestBody MatchingRequest.MatchingRequestCreate reqDto,
            @AuthenticationPrincipal AuthUser authUser
    ){
        ApiResponse<Void> apiResponse = matchingService.createMatching(reqDto,authUser);
        return ApiResponse.of(apiResponse);
    }

    @Operation(summary = "매칭 수정", description = "매칭 수정 API")
    @PutMapping("/{matchingId}")
    public ResponseEntity<ApiResponse<Void>> updateMatching(
            @NotNull(message = "매칭 ID는 공백 일 수 없습니다") @Positive(message = "매칭 ID는 0을 초과 해야 합니다") @PathVariable Long matchingId,
            @Valid @RequestBody MatchingRequest.MatchingRequestUpdate reqDto,
            @AuthenticationPrincipal AuthUser authUser
    ){
        ApiResponse<Void> apiResponse = matchingService.updateMatching(matchingId,reqDto,authUser);
        return ApiResponse.of(apiResponse);
    }

    @Operation(summary = "매칭 삭제", description = "매칭 삭제 API")
    @DeleteMapping("/{matchingId}")
    public ResponseEntity<ApiResponse<Void>> deleteMatching(
            @NotNull(message = "매칭 ID는 공백 일 수 없습니다") @Positive(message = "매칭 ID는 0을 초과 해야 합니다") @PathVariable Long matchingId,
            @AuthenticationPrincipal AuthUser authUser
    ){
        ApiResponse<Void> apiResponse = matchingService.deleteMatching(matchingId,authUser);
        return ApiResponse.of(apiResponse);
    }

}

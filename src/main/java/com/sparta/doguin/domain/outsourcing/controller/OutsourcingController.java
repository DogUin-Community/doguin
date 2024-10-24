package com.sparta.doguin.domain.outsourcing.controller;

import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingRequest;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingResponse;
import com.sparta.doguin.domain.outsourcing.service.OutsourcingService;
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
@RequestMapping("/api/v1/outsourcing")
public class OutsourcingController {
    private final OutsourcingService outsourcingService;

    @GetMapping("/{outsourcingId}")
    public ResponseEntity<ApiResponse<OutsourcingResponse>> getOutsourcing(@PathVariable Long outsourcingId){
        ApiResponse<OutsourcingResponse> apiResponse = outsourcingService.getOutsourcing(outsourcingId);
        return ApiResponse.of(apiResponse);
    }

    /**
     * 전체 외주 공고들 확인 가능
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<OutsourcingResponse>>> getAllOutsourcing(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "desc", required = false) String sort,
            @RequestParam(required = false) AreaType area
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sort);
        Pageable pageable = PageRequest.of(page, size, direction,"createdAt");
        ApiResponse<Page<OutsourcingResponse>> apiResponse = outsourcingService.getAllOutsourcing(pageable,area);
        return ApiResponse.of(apiResponse);
    }



    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createOutsourcing(@Valid @RequestBody OutsourcingRequest.OutsourcingRequestCreate reqDto, @AuthenticationPrincipal AuthUser authUser){
        ApiResponse<Void> apiResponse = outsourcingService.createOutsourcing(reqDto,authUser);
        return ApiResponse.of(apiResponse);
    }

    @PutMapping("/{outsourcingId}")
    public ResponseEntity<ApiResponse<Void>> updateOutsourcing(@PathVariable Long outsourcingId, @RequestBody OutsourcingRequest.OutsourcingRequestUpdate reqDto, @AuthenticationPrincipal AuthUser authUser){
        ApiResponse<Void> apiResponse = outsourcingService.updateOutsourcing(outsourcingId,reqDto,authUser);
        return ApiResponse.of(apiResponse);
    }

    @DeleteMapping("/{outsourcingId}")
    public ResponseEntity<ApiResponse<Void>> deleteOutsourcing(@PathVariable Long outsourcingId,@AuthenticationPrincipal AuthUser authUser){
        ApiResponse<Void> apiResponse = outsourcingService.deleteOutsourcing(outsourcingId,authUser);
        return ApiResponse.of(apiResponse);
    }

}

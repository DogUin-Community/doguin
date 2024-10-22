package com.sparta.doguin.domain.outsourcing.controller;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingRequestDto;
import com.sparta.doguin.domain.outsourcing.model.OutsourctingResponse;
import com.sparta.doguin.domain.outsourcing.service.OutsourcingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/outsourcing")
public class OutsourcingController {
    private final OutsourcingService outsourcingService;

    @GetMapping("/{outsourcingId}")
    public ResponseEntity<ApiResponse<OutsourctingResponse>> getOutsourcing(@PathVariable Long outsourcingId){
        ApiResponse<OutsourctingResponse> apiResponse = outsourcingService.getOutsourcing(outsourcingId);
        return ApiResponse.of(apiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createOutsourcing(@RequestBody OutsourcingRequestDto requestDto){
        ApiResponse<Void> apiResponse = outsourcingService.createOutsourcing(requestDto);
        return ApiResponse.of(apiResponse);
    }

    @PutMapping("/{outsourcingId}")
    public ResponseEntity<ApiResponse<Void>> updateOutsourcing(@PathVariable Long outsourcingId,  OutsourcingRequestDto reqDto){
        ApiResponse<Void> apiResponse = outsourcingService.updateOutsourcing(outsourcingId,reqDto);
        return ApiResponse.of(apiResponse);
    }

    @DeleteMapping("/{outsourcingId}")
    public ResponseEntity<ApiResponse<Void>> deleteOutsourcing(@PathVariable Long outsourcingId){
        ApiResponse<Void> apiResponse = outsourcingService.deleteOutsourcing(outsourcingId);
        return ApiResponse.of(apiResponse);
    }

}

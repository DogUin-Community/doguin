package com.sparta.doguin.domain.outsourcing.controller;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingRequest;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingRequestSearch;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingRequestSearchAll;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingResponse;
import com.sparta.doguin.domain.outsourcing.service.OutsourcingService;
import com.sparta.doguin.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.sparta.doguin.domain.common.response.ApiResponseOutsourcingEnum.OUTSOURCING_SUCCESS;


@Tag(name = "외주 API",description = "외주 관련된 API를 확인 할 수 있습니다")
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/outsourcings")
public class OutsourcingController {
    private final OutsourcingService outsourcingService;


    @Operation(summary = "특정 Id의 외주 단건 가져오기", description = "단건 외주 가져오기 API")
    @GetMapping("/{outsourcingId}")
    public ResponseEntity<ApiResponse<OutsourcingResponse>> getOutsourcing(
            @NotNull(message = "외주 ID는 공백 일 수 없습니다") @Positive(message = "외주 ID는 0을 초과 해야 합니다") @PathVariable Long outsourcingId
    ){
        OutsourcingResponse response = outsourcingService.getOutsourcing(outsourcingId);
        ApiResponse<OutsourcingResponse> apiResponse = ApiResponse.of(OUTSOURCING_SUCCESS, response);
        return ApiResponse.of(apiResponse);
    }

    @Operation(summary = "외주 생성", description = "외주 생성 API")
    @PostMapping
    public ResponseEntity<ApiResponse<OutsourcingResponse>> createOutsourcing(
            @Valid @RequestPart OutsourcingRequest.OutsourcingRequestCreate outsourcingRequestCreate,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestPart(required = false) List<MultipartFile> files
    ){
        ApiResponse<OutsourcingResponse> apiResponse = outsourcingService.createOutsourcing(outsourcingRequestCreate,authUser,files);
        return ApiResponse.of(apiResponse);
    }

    @Operation(summary = "외주 수정", description = "외주 수정 API")
    @PutMapping("/{outsourcingId}")
    public ResponseEntity<ApiResponse<Void>> updateOutsourcing(
            @NotNull(message = "외주 ID는 공백 일 수 없습니다") @Positive(message = "외주 ID는 0을 초과 해야 합니다") @PathVariable Long outsourcingId,
            @RequestPart OutsourcingRequest.OutsourcingRequestUpdate outsourcingRequestUpdate,
            @RequestPart(required = false) List<MultipartFile> files,
            @AuthenticationPrincipal AuthUser authUser
    ){
        ApiResponse<Void> apiResponse = outsourcingService.updateOutsourcing(outsourcingId,outsourcingRequestUpdate,authUser,files);
        return ApiResponse.of(apiResponse);
    }

    @Operation(summary = "외주 삭제", description = "외주 삭제 API")
    @DeleteMapping("/{outsourcingId}")
    public ResponseEntity<ApiResponse<Void>> deleteOutsourcing(
            @NotNull(message = "외주 ID는 공백 일 수 없습니다") @Positive(message = "외주 ID는 0을 초과 해야 합니다") @PathVariable Long outsourcingId,
            @AuthenticationPrincipal AuthUser authUser
    ){
        ApiResponse<Void> apiResponse = outsourcingService.deleteOutsourcing(outsourcingId,authUser);
        return ApiResponse.of(apiResponse);
    }

    /**
     * 전체 외주 공고들 확인 가능
     */
    @Operation(summary = "외주 다건 조회 ", description = "외주 다건 조회 API")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<OutsourcingResponse>>> getAllOutsourcing(
            @ModelAttribute @Valid OutsourcingRequestSearch request
    ) {
        Sort.Direction direction = Sort.Direction.fromString(request.getSort());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), direction,"createdAt");
        Page<OutsourcingResponse> response = outsourcingService.getAllOutsourcing(pageable,request.getArea());
        ApiResponse<Page<OutsourcingResponse>> apiResponse = ApiResponse.of(OUTSOURCING_SUCCESS,response);
        return ApiResponse.of(apiResponse);
    }

    /**
     * 제목,닉네임,본문 데이터가 들어온것에 맞게 외주 데이터 반환 컨트롤러
     */
    @Operation(summary = "외주 다건 검색", description = "외주 다건 검색 API")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<OutsourcingResponse>>> search(
            @ModelAttribute @Valid OutsourcingRequestSearchAll request
    ){
        Sort.Direction direction = Sort.Direction.fromString(request.getSort());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), direction,"createdAt");
        Page<OutsourcingResponse> response = outsourcingService.search(pageable,request.getTitle(),request.getNickname(),request.getContent());
        ApiResponse<Page<OutsourcingResponse>> apiResponse = ApiResponse.of(OUTSOURCING_SUCCESS,response);
        return ApiResponse.of(apiResponse);
    }

}

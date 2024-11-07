package com.sparta.doguin.domain.outsourcing.controller;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingRequest;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingResponse;
import com.sparta.doguin.domain.outsourcing.service.OutsourcingService;
import com.sparta.doguin.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.sparta.doguin.domain.common.response.ApiResponseOutsourcingEnum.OUTSOURCING_SUCCESS;


@Tag(name = "외주 API",description = "외주 관련된 API를 확인 할 수 있습니다")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/outsourcings")
public class OutsourcingController {
    private final OutsourcingService outsourcingService;


    @Operation(summary = "특정 Id의 외주 단건 가져오기", description = "단건 외주 가져오기 API")
    @GetMapping("/{outsourcingId}")
    public ResponseEntity<ApiResponse<OutsourcingResponse>> getOutsourcing(@PathVariable Long outsourcingId){
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
            @PathVariable Long outsourcingId,
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
            @PathVariable Long outsourcingId,
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
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "desc", required = false) String sort,
            @RequestParam(required = false) AreaType area
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sort);
        Pageable pageable = PageRequest.of(page, size, direction,"createdAt");
        Page<OutsourcingResponse> response = outsourcingService.getAllOutsourcing(pageable,area);
        ApiResponse<Page<OutsourcingResponse>> apiResponse = ApiResponse.of(OUTSOURCING_SUCCESS,response);
        return ApiResponse.of(apiResponse);
    }

    /**
     * 제목,닉네임,본문 데이터가 들어온것에 맞게 외주 데이터 반환 컨트롤러
     */
    @Operation(summary = "외주 다건 검색", description = "외주 다건 검색 API")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<OutsourcingResponse>>> search(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "desc", required = false) String sort,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String content
    ){
        Sort.Direction direction = Sort.Direction.fromString(sort);
        Pageable pageable = PageRequest.of(page, size, direction,"createdAt");
        Page<OutsourcingResponse> response = outsourcingService.search(pageable,title,nickname,content);
        ApiResponse<Page<OutsourcingResponse>> apiResponse = ApiResponse.of(OUTSOURCING_SUCCESS,response);
        return ApiResponse.of(apiResponse);
    }

}

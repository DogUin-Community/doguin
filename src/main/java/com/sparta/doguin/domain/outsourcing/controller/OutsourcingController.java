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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.sparta.doguin.domain.common.response.ApiResponseOutsourcingEnum.OUTSOURCING_SUCCESS;


@Tag(name = "외주 API",description = "외주 관련된 API를 확인 할 수 있습니다")
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/outsourcings")
public class OutsourcingController {
    private final OutsourcingService outsourcingService;


    @Operation(summary = "특정 Id의 외주 단건 가져오기", description = "단건 외주 가져오기 API")
    @GetMapping("/detail/{outsourcingId}")
    public String getOutsourcing(@PathVariable Long outsourcingId, Model model) {
        // 특정 ID의 외주 데이터를 서비스에서 가져옴
        OutsourcingResponse response = outsourcingService.getOutsourcing(outsourcingId);

        // 가져온 데이터를 모델에 추가하여 Thymeleaf 템플릿에서 사용 가능하게 설정
        model.addAttribute("outsourcing", response);

        // Thymeleaf 템플릿 이름 반환
        return "outsourcings/detail";  // outsourcing_detail.html 템플릿을 렌더링
    }

    // 외주 생성 페이지를 반환하는 메서드
    @GetMapping("/create")
    public String createOutsourcingForm(Model model) {
        // 필요시 모델에 기본값이나 추가 데이터를 전달할 수 있음

        // 외주 생성 폼을 렌더링할 Thymeleaf 템플릿 이름을 반환
        return "outsourcings/create";
    }

    @Operation(summary = "외주 생성", description = "외주 생성 API")
    @PostMapping
    public ResponseEntity<ApiResponse<OutsourcingResponse>> createOutsourcing(
            @Valid @RequestPart OutsourcingRequest.OutsourcingRequestCreate outsourcingRequestCreate,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestPart(required = false) List<MultipartFile> files
    ){
        OutsourcingResponse response = outsourcingService.createOutsourcing(outsourcingRequestCreate,authUser,files);
        ApiResponse<OutsourcingResponse> apiResponse = new ApiResponse<>(OUTSOURCING_SUCCESS, response);
        return ApiResponse.of(apiResponse);
    }

    @GetMapping("/edit/{outsourcingId}")
    public String updateOutsourcingForm(@PathVariable Long outsourcingId,Model model) {
        // 필요시 모델에 기본값이나 추가 데이터를 전달할 수 있음
        OutsourcingResponse.OutsourcingResponseGet outsourcing =  (OutsourcingResponse.OutsourcingResponseGet) outsourcingService.getOutsourcing(outsourcingId);
        // 모델에 추가하여 수정 페이지에 전달
        // DateTimeFormatter를 사용하여 LocalDateTime을 문자열로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = outsourcing.createdAt().format(formatter);

        model.addAttribute("outsourcing", outsourcing);
        model.addAttribute("formattedDateTime", formattedDateTime);  // 포맷된 문자열 추가
        // 외주 생성 폼을 렌더링할 Thymeleaf 템플릿 이름을 반환
        return "outsourcings/edit";
    }

    @Operation(summary = "외주 수정", description = "외주 수정 API")
    @PutMapping("{outsourcingId}")
    public ResponseEntity<ApiResponse<Void>> updateOutsourcing(
            @PathVariable Long outsourcingId,
            @RequestPart OutsourcingRequest.OutsourcingRequestUpdate outsourcingRequestUpdate,
            @RequestPart(required = false) List<MultipartFile> files,
            @AuthenticationPrincipal AuthUser authUser

    ){
        outsourcingService.updateOutsourcing(outsourcingId,outsourcingRequestUpdate,authUser,files);
        ApiResponse<Void> apiResponse = new ApiResponse<>(OUTSOURCING_SUCCESS);
        return ApiResponse.of(apiResponse);
    }

    @Operation(summary = "외주 삭제", description = "외주 삭제 API")
    @DeleteMapping("/{outsourcingId}")
    public ResponseEntity<ApiResponse<Void>> deleteOutsourcing(
            @PathVariable Long outsourcingId,
            @AuthenticationPrincipal AuthUser authUser
    ){
        outsourcingService.deleteOutsourcing(outsourcingId,authUser);
        ApiResponse<Void> apiResponse = new ApiResponse<>(OUTSOURCING_SUCCESS);
        return ApiResponse.of(apiResponse);
    }

    /**
     * 제목,닉네임,본문 데이터가 들어온것에 맞게 외주 데이터 반환 컨트롤러
     */
    @Operation(summary = "외주 다건 조회 ", description = "외주 다건 조회 API")
    @GetMapping("/list")
    public String getAllOutsourcing(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "desc", required = false) String sort,
            @RequestParam(required = false) AreaType area,
            Model model
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sort);
        Pageable pageable = PageRequest.of(page, size, direction,"createdAt");
        Page<OutsourcingResponse> response = outsourcingService.getAllOutsourcing(pageable, area);

        // ApiResponse를 Model에 추가
        ApiResponse<Page<OutsourcingResponse>> apiResponse = ApiResponse.of(OUTSOURCING_SUCCESS, response);
        model.addAttribute("apiResponse", apiResponse.getData());

        return "outsourcings/list";
    }

}

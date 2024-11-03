package com.sparta.doguin.domain.attachment.controller;

import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentDeleteService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentDownloadService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUpdateService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUploadService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseFileEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "파일 API",description = "파일 관련된 API를 확인 할 수 있습니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attachments")
public class AttachmentController {
    private final AttachmentUploadService attachmentUploadService;
    private final AttachmentDownloadService attachmentDownloadService;
    private final AttachmentUpdateService attachmentUpdateService;
    private final AttachmentDeleteService attachmentDeleteService;

    @Operation(summary = "파일 생성", description = "파일 생성 API")
    @PostMapping("/targetId/{targetId}/target/{target}")
    public ResponseEntity<ApiResponse<Void>> attachmentUpload(
            @PathVariable Long targetId,
            @PathVariable AttachmentTargetType target,
            @RequestPart List<MultipartFile> files,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        attachmentUploadService.upload(files, authUser, targetId, target);
        ApiResponse<Void> apiResponse = new ApiResponse<>(ApiResponseFileEnum.FILE_OK);
        return ApiResponse.of(apiResponse);
    }

    @Operation(summary = "파일 ID로, 파일들 조회하기", description = "파일 다건 조회 API")
    @GetMapping("fileIds/{fileIds}")
    public ResponseEntity<ApiResponse<List<String>>> attachmentDownload(
            @PathVariable List<Long> fileIds
    ){
        ApiResponse<List<String>> apiResponse = attachmentDownloadService.download(fileIds);
        return ApiResponse.of(apiResponse);
    }

    @Operation(summary = "파일들 다건 수정", description = "파일들 다건 수정 API")
    @PutMapping("fileIds/{fileIds}")
    public void attachmentUpdate(
            @PathVariable List<Long> fileIds,
            @RequestPart List<MultipartFile> files,
            @AuthenticationPrincipal AuthUser authUser
    ){
        attachmentUpdateService.update(files,fileIds,authUser);
    }

    @Operation(summary = "파일들 다건 삭제", description = "파일들 다건 삭제 API")
    @DeleteMapping("fileIds/{fileIds}")
    public void attachmentDelete(
            @PathVariable List<Long> fileIds,
            @AuthenticationPrincipal AuthUser authUser
    ){
        attachmentDeleteService.delete(authUser,fileIds);
    }


}

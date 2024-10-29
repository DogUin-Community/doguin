package com.sparta.doguin.domain.attachment.controller;

import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentDeleteService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentDownloadService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUpdateService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUploadService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseFileEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attachments")
public class AttachmentController {
    private final AttachmentUploadService attachmentUploadService;
    private final AttachmentDownloadService attachmentDownloadService;
    private final AttachmentUpdateService attachmentUpdateService;
    private final AttachmentDeleteService attachmentDeleteService;

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

    @GetMapping("fileIds/{fileIds}")
    public ResponseEntity<ApiResponse<List<String>>> attachmentDownload(
            @PathVariable List<Long> fileIds
    ){
        ApiResponse<List<String>> apiResponse = attachmentDownloadService.download(fileIds);
        return ApiResponse.of(apiResponse);
    }

    @PutMapping("fileIds/{fileIds}")
    public void attachmentUpdate(
            @PathVariable List<Long> fileIds,
            @RequestPart List<MultipartFile> files,
            @AuthenticationPrincipal AuthUser authUser
    ){
        attachmentUpdateService.update(files,fileIds,authUser);
    }

    @DeleteMapping("fileIds/{fileIds}")
    public void attachmentDelete(
            @PathVariable List<Long> fileIds,
            @AuthenticationPrincipal AuthUser authUser
    ){
        attachmentDeleteService.delete(authUser,fileIds);
    }


}

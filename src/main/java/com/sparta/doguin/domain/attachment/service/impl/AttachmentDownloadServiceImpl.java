package com.sparta.doguin.domain.attachment.service.impl;

import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentDownloadService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentGetService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sparta.doguin.domain.common.response.ApiResponseFileEnum.FILE_OK;

@Service
@RequiredArgsConstructor
public class AttachmentDownloadServiceImpl implements AttachmentDownloadService {
    private final AttachmentGetService attachmentGetService;

    /**
     * @title 파일 고유식별자를 통하여, 해당 파일이 S3에 저장되있는 경로를 반환합니다
     * @description 클라이언트로부터 파일의 고유식별자들을 받은후, 해당 ID들을 DB에서 찾고나서 파일의 경로들을 반환합니다
     *
     * @param attachmentIds 조회할 파일 아이디들
     * @return ApiResponse<List<String>> 파일아이디르의 파일 경로를 반환합니다
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    @Override
    public ApiResponse<List<String>> download(List<Long> attachmentIds) {
        List<String> attachmentFilePaths = attachmentGetService.getAllAttachmentPath(attachmentIds);
        return ApiResponse.of(FILE_OK,attachmentFilePaths);
    }
}
package com.sparta.doguin.domain.attachment.service.interfaces;

import com.sparta.doguin.domain.common.response.ApiResponse;

import java.util.List;

public interface AttachmentDownloadService {
    ApiResponse<List<String>> download(List<Long> fileIds);
}

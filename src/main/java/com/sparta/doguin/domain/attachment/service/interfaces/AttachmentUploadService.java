package com.sparta.doguin.domain.attachment.service.interfaces;

import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;
import com.sparta.doguin.domain.attachment.model.AttachmentResponse;
import com.sparta.doguin.domain.common.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentUploadService {
    ApiResponse<List<AttachmentResponse.AttachmentResponseGet>> upload(List<MultipartFile> files, AuthUser authUser, Long targetId, AttachmentTargetType targetType);
}

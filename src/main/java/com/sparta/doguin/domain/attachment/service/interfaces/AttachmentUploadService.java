package com.sparta.doguin.domain.attachment.service.interfaces;

import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentUploadService {
    void upload(List<MultipartFile> files, AuthUser authUser, Long targetId, AttachmentTargetType target);
}

package com.sparta.doguin.domain.attachment.service.interfaces;

import com.sparta.doguin.security.AuthUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentUpdateService {
    void update(List<MultipartFile> updateAttachments, List<Long> fileIds,AuthUser authUser);
}

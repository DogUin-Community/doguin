package com.sparta.doguin.domain.attachment.service.interfaces;

import com.sparta.doguin.config.security.AuthUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentUpdateService {
    void update(List<MultipartFile> files, AuthUser authUser, List<Long> fileIds);
}

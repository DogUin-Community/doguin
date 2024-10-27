package com.sparta.doguin.domain.attachment.service.component;

import org.springframework.web.multipart.MultipartFile;

public class AttachmentUtil {
    public static String getExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
}

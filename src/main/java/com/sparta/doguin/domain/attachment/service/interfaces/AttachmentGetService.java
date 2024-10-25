package com.sparta.doguin.domain.attachment.service.interfaces;

import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;

import java.util.List;

public interface AttachmentGetService {
    List<String> getFilePath(Long userId, Long targetId, AttachmentTargetType targetType);
    List<String> getAllFilePath(Long targetId, AttachmentTargetType targetType);
    List<Long> getFileIds(Long userId, Long targetId, AttachmentTargetType targetType);
}

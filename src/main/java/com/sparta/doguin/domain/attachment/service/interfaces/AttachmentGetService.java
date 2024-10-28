package com.sparta.doguin.domain.attachment.service.interfaces;

import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;
import com.sparta.doguin.domain.attachment.entity.Attachment;

import java.util.List;

public interface AttachmentGetService {
    List<String> getAllAttachmentPath(Long userId, Long targetId, AttachmentTargetType targetType);
    List<String> getAllAttachmentPath(Long targetId, AttachmentTargetType targetType);
    List<String> getAllAttachmentPath(List<Long> attachmentIds);

    List<Long> getFileIds(Long userId, Long targetId, AttachmentTargetType targetType);

    List<Attachment> getFiles(List<Long> attachmentIds);

}

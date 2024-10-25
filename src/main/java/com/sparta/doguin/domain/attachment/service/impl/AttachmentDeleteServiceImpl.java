package com.sparta.doguin.domain.attachment.service.impl;


import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.attachment.entity.Attachment;
import com.sparta.doguin.domain.attachment.repository.AttachmentRepository;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentDeleteService;
import com.sparta.doguin.domain.attachment.service.s3.S3Service;
import com.sparta.doguin.domain.attachment.validate.AttachmentValidator;
import com.sparta.doguin.domain.common.exception.FileException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.sparta.doguin.domain.common.response.ApiResponseFileEnum.FILE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AttachmentDeleteServiceImpl implements AttachmentDeleteService {
    private final AttachmentRepository attachmentRepository;
    private final S3Service s3Service;

    /**
     * @title 파일의 고유식별자로 S3,DB의 파일들을 삭제합니다
     * @description 파일중 하나라도 이상한게 있다면, 전부 롤백 시킵니다
     *
     * @param authUser
     * @param fileIds
     */
    @Transactional
    @Override
    public void delete(AuthUser authUser, List<Long> fileIds) {
        List<Attachment> attachments = new ArrayList<>();
        for ( Long fileId : fileIds ) {
            Attachment attachment = attachmentRepository.findById(fileId).orElseThrow(() -> new FileException(FILE_NOT_FOUND));
            AttachmentValidator.isMe(authUser.getUserId(),attachment.getUser().getId());
            attachments.add(attachment);
        }
        s3Service.deleteAll(attachments);
        attachmentRepository.deleteAll(attachments);
    }
}

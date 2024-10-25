package com.sparta.doguin.domain.attachment.service.impl;


import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.attachment.entity.Attachment;
import com.sparta.doguin.domain.attachment.repository.AttachmentRepository;
import com.sparta.doguin.domain.attachment.service.component.PathService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUpdateService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUploadService;
import com.sparta.doguin.domain.attachment.service.s3.S3Service;
import com.sparta.doguin.domain.attachment.validate.AttachmentValidator;
import com.sparta.doguin.domain.common.exception.FileException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.sparta.doguin.domain.common.response.ApiResponseFileEnum.FILE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AttachmentUpdateServiceImpl implements AttachmentUpdateService {
    private final S3Service s3Service;
    private final AttachmentUploadService fileUploadService;
    private final PathService pathService;
    private final AttachmentRepository attachmentRepository;

    @Transactional
    @Override
    public void update(List<MultipartFile> files, AuthUser authUser,List<Long> fileIds) {
        List<Attachment> attachments = new ArrayList<>();
        for ( Long fileId : fileIds ) {
            Attachment attachment = attachmentRepository.findById(fileId).orElseThrow(() -> new FileException(FILE_NOT_FOUND));
            AttachmentValidator.isMe(authUser.getUserId(),attachment.getUser().getId());
            Attachment findAttachment = new Attachment(
                    attachment.getId(),
                    attachment.getUser(),
                    attachment.getTargetId(),
                    attachment.getFile_absolute_path(),
                    attachment.getFile_relative_path(),
                    attachment.getFile_original_name(),
                    attachment.getFile_size(),
                    attachment.getTarget()
            );
            attachments.add(findAttachment);
        }
        List<Attachment> updateAttachments = new ArrayList<>();
        for ( int i=0; i<attachments.size(); i++ ) {
            Attachment originAttachment = attachments.get(i);
            MultipartFile updateMultipartFile = files.get(i);
            Path path = pathService.mkPath(updateMultipartFile,authUser,originAttachment.getTargetId(),originAttachment.getTarget());
            String fullPath = pathService.mkfullPath(path);
            Attachment attachment = new Attachment(
                    originAttachment.getId(),
                    originAttachment.getUser(),
                    originAttachment.getTargetId(),
                    fullPath,
                    path.toString(),
                    updateMultipartFile.getOriginalFilename(),
                    updateMultipartFile.getSize(),
                    originAttachment.getTarget()
            );
            updateAttachments.add(attachment);
            s3Service.upload(path,updateMultipartFile);
        }

        s3Service.deleteAll(attachments);
        attachmentRepository.deleteAll(attachments);
        attachmentRepository.saveAll(updateAttachments);
    }
}

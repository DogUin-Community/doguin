package com.sparta.doguin.domain.attachment.service.impl;


import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.attachment.entity.Attachment;
import com.sparta.doguin.domain.attachment.repository.AttachmentRepository;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentDeleteService;
import com.sparta.doguin.domain.attachment.service.s3.S3Service;
import com.sparta.doguin.domain.attachment.validate.AttachmentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttachmentDeleteServiceImpl implements AttachmentDeleteService {
    private final AttachmentRepository attachmentRepository;
    private final S3Service s3Service;

    /**
     * @title 파일의 고유식별자로 S3,DB의 파일들을 삭제합니다
     * @description 파일중 하나라도 이상한게 있다면, 전부 롤백 시킵니다
     *
     * @param authUser 로그인한 유저
     * @param attachmentIds 삭제할 파일 아이디들
     */
    @Transactional
    @Override
    public void delete(AuthUser authUser, List<Long> attachmentIds) {
        List<Attachment> attachments = attachmentRepository.findAllByAttachment(attachmentIds);
        AttachmentValidator.isCountEqual(attachmentIds.size(),attachments.size());
        for ( int i=0 ;i<attachments.size(); i++ ){
            Attachment attachment = attachments.get(i);
            AttachmentValidator.isMe(authUser.getUserId(),attachment.getUser().getId());
            attachmentRepository.delete(attachment);
        }
        s3Service.deleteAllAsync(attachments);
    }
}

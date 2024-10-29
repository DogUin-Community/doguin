package com.sparta.doguin.domain.attachment.service.impl;


import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.attachment.entity.Attachment;
import com.sparta.doguin.domain.attachment.repository.AttachmentRepository;
import com.sparta.doguin.domain.attachment.service.component.PathService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentGetService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUpdateService;
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

import static com.sparta.doguin.domain.common.response.ApiResponseFileEnum.FILE_IO_ERROR;

@Service
@RequiredArgsConstructor
public class AttachmentUpdateServiceImpl implements AttachmentUpdateService {
    private final S3Service s3Service;
    private final PathService pathService;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentGetService attachmentGetService;

    /**
     * @title 파일 DB와, S3에 파일 데이터만 업데이트 하는 메서드
     * @description1 기존 파일아이디로 파링 이외의 데이터 ( id,user ... ) 등을 가져와서, 데이터를 넣어줍니다
     * @description2 수정된 파일들을 넣어줍니다
     *
     * @param updateFiles 수정할 파일들
     * @param authUser 로그인 한 유저
     * @param fileIds 기존 파일들
     * @since 1.0
     * @author 김경민
     */

    @Transactional
    @Override
    public void update(List<MultipartFile> updateFiles, List<Long> fileIds,AuthUser authUser) {
        List<Attachment> prvAattachments = attachmentGetService.getFiles(fileIds);
        AttachmentValidator.isCountEqual(fileIds.size(), prvAattachments.size());
        updateAttachmentsWithNewFiles(prvAattachments,updateFiles,authUser);
    }

    /**
     * @title 새로운 파일을 S3에 저장시키고, 기존 파일은 S3에서 삭제, DB에서 삭제 시키는 메서드
     *
     * @param prvAattachments 이전 파일목록
     * @param updateAttachments 수정할 파일목록
     * @param authUser 로그인한 유저
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    protected void updateAttachmentsWithNewFiles(List<Attachment> prvAattachments,List<MultipartFile> updateAttachments,AuthUser authUser) {
        List<byte[]> fileBytesList = new ArrayList<>();
        List<Path> paths = new ArrayList<>();
        for (int i = 0; i < prvAattachments.size(); i++) {
            Attachment originAttachment = prvAattachments.get(i);
            MultipartFile updateFile = updateAttachments.get(i);
            AttachmentValidator.isInExtension(updateFile);
            AttachmentValidator.isMe(authUser.getUserId(), originAttachment.getUser().getId());
            AttachmentValidator.isSizeBig(updateFile);
            Path path = pathService.mkPath(updateFile, authUser, originAttachment.getTargetId(), originAttachment.getTarget());
            String fullPath = pathService.mkfullPath(path);
            Attachment attachment = new Attachment(
                    originAttachment.getId(),
                    originAttachment.getUser(),
                    originAttachment.getTargetId(),
                    fullPath,
                    path.toString(),
                    updateFile.getOriginalFilename(),
                    updateFile.getSize(),
                    originAttachment.getTarget()
            );
            paths.add(path);
            try {
                fileBytesList.add(updateFile.getBytes());
            } catch (Exception e){
                throw new FileException(FILE_IO_ERROR);
            }
            attachmentRepository.save(attachment);
        }
        s3Service.uploadAllAsync(paths,fileBytesList);
        s3Service.deleteAllAsync(prvAattachments);
    }
}

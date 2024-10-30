package com.sparta.doguin.domain.attachment.service.impl;

import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;
import com.sparta.doguin.domain.attachment.entity.Attachment;
import com.sparta.doguin.domain.attachment.repository.AttachmentRepository;
import com.sparta.doguin.domain.attachment.service.component.PathService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUploadService;
import com.sparta.doguin.domain.attachment.service.s3.S3Service;
import com.sparta.doguin.domain.attachment.validate.AttachmentValidator;
import com.sparta.doguin.domain.common.exception.FileException;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.sparta.doguin.domain.common.response.ApiResponseFileEnum.FILE_IO_ERROR;

@Service
@RequiredArgsConstructor
public class AttachmentUploadServiceImpl implements AttachmentUploadService {
    private final AttachmentRepository attachmentRepository;
    private final PathService pathService;
    private final S3Service s3Service;

    /**
     * @title 파일들을 DB와, S3에 저장하는 메서드
     * @description1 파일들을 S3에 저장시키고, save 메서드를 통해 반환된 고유식별자를 반환합니다
     * @description2 최종적으로 아래와 같은 url을 만들고, 해당 경로에 S3에 파일저장후, DB경로에도 저 이름 그대로 넣어준다
     * @description3 https:/doguin.s3.ap-northeast-2.amazonaws.com/user/1/targetId/1/outsourcing/dba64057-0135-455b-b27d-f88300e2ba3f/90b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(3).jpg
     * @description4 들어온 파일중 하나라도 이상할경우, DB S3전부 롤백
     *
     * @param files 저장할 파일들
     * @param authUser 로그인된 유저
     * @param targetId 타겟 ID
     * @param target 타겟 설정
     * @return ApiResponse<List<AttachmentResponse.AttachmentResponseGet>> 성공 응답과, 정상적으로 저장된 파일들의 ID 반환
     * @throws FileException 작업중 문제 발생시 생기는 파일예외
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    @Override
    public void upload(List<MultipartFile> files, AuthUser authUser, Long targetId, AttachmentTargetType target){
        User user = User.fromAuthUser(authUser);
        List<byte[]> fileBytesList = new ArrayList<>();
        List<String> paths = new ArrayList<>();
        for ( MultipartFile file : files ) {
            AttachmentValidator.isInExtension(file);
            AttachmentValidator.isSizeBig(file);
            String path = pathService.mkPath(file,authUser,targetId,target);
            try {
                fileBytesList.add(file.getBytes());
            } catch (Exception e){
                throw new FileException(FILE_IO_ERROR);
            }

            paths.add(path);
            saveAttachmentGetId(path,file,user,targetId,target);
        }
        s3Service.uploadAllAsync(paths,fileBytesList);
    }

    /**
     * @title 사용자로부터 들러온 파일들의 정보를, DB에 저장하는 메서드
     * @description S3에 파일이 저장될떄, 해당 파일의 데이터를 사용하여 DB에 저장시킨다
     *
     * @param path 저장할 경로 (상대경로)
     * @param file 저장할 파일객체의 정보
     * @param user 유저의 정보
     * @param targetId 타겟 ID
     * @param target 타겟 설정
     * @return 정상적으로 성공된 PK를 반환
     * @throws FileException 작업중 문제 발생시 생기는 파일예외
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    protected void saveAttachmentGetId(String path, MultipartFile file,User user, Long targetId, AttachmentTargetType target){
        String fullPath = pathService.mkfullPath(path);
        Attachment attachment = Attachment.builder()
                .user(user)
                .targetId(targetId)
                .attachment_absolute_path(fullPath)
                .attachment_relative_path(path)
                .attachment_original_name(file.getOriginalFilename())
                .attachment_size(file.getSize())
                .target(target)
                .build();
        attachmentRepository.save(attachment);
    }
}

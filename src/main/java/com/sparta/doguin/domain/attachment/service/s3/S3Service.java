package com.sparta.doguin.domain.attachment.service.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sparta.doguin.domain.attachment.entity.Attachment;
import com.sparta.doguin.domain.common.exception.FileException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

import static com.sparta.doguin.domain.common.response.ApiResponseFileEnum.FILE_IO_ERROR;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    /**
     * @title S3에 파일 업로드 메서드
     *
     * @param filePath 업로드할 경로
     * @param file 업로드할 파일
     * @throws FileException 파일 업로드중 예외 발생 처리 로직
     * @since 1.0
     * @author 김경민
     */
    public void upload(Path filePath, MultipartFile file) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket,filePath.toString(),file.getInputStream(),metadata);
        } catch (Exception e) {
            throw new FileException(FILE_IO_ERROR);
        }
    }

    /**
     * @title 모든 파일을 S3에서 제거 하는 메서드
     *
     * @param attachments 지울 파일 목록
     * @since 1.0
     * @author 김경민
     */
    public void deleteAll(List<Attachment> attachments){
        for (Attachment attachment : attachments) {
            amazonS3Client.deleteObject(bucket,attachment.getAttachment_relative_path());
        }
    }

    /**
     * 단일 파일을 S3에서 제거하는 메서드
     *
     * @param attachment 제거할 파일
     * @since 1.0
     * @author 김경민
     */
    public void delete(Attachment attachment){
        amazonS3Client.deleteObject(bucket,attachment.getAttachment_relative_path());
    }


}


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

    public void deleteAll(List<Attachment> files){
        for (Attachment file : files) {
            amazonS3Client.deleteObject(bucket,file.getFile_relative_path());
        }
    }

    public void delete(Attachment file){
        amazonS3Client.deleteObject(bucket,file.getFile_relative_path());
    }

    public void delete(Path path) {
        amazonS3Client.deleteObject(bucket,path.toString());
    }
}


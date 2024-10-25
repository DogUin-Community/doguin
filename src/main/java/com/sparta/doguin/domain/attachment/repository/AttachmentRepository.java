package com.sparta.doguin.domain.attachment.repository;

import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;
import com.sparta.doguin.domain.attachment.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    // 유저 + 타겟 + 타겟타입 으로 찾은 파일의 s3 경로를 페이지 네비게이션으로 가져옴 (엔티티에 있는것들)
    @Query("SELECT a.file_absolute_path from Attachment a " +
            "WHERE a.user.id = :userId " +
            "AND a.targetId = :targetId " +
            "AND a.target = :target "
    )
    List<String> findAllFilePathByUserIdAndTagertIdAndTarget(Long userId, Long targetId, AttachmentTargetType target);

    @Query("SELECT a.file_absolute_path from Attachment a " +
            "WHERE a.targetId = :targetId " +
            "AND a.target = :target "
    )
    List<String> findAllFilePathByTagertIdAndTarget(Long targetId, AttachmentTargetType target);

    @Query("SELECT a.file_absolute_path from Attachment a " +
            "WHERE a.id = :fileId "
    )
    String findAbsolutePathById(Long fileId);

    @Query("SELECT a.id from Attachment a " +
            "WHERE a.user.id = :userId " +
            "AND a.targetId = :targetId " +
            "AND a.target = :target "
    )
    List<Long> findAllFileIdByUserIdAndTagertIdAndTarget(Long userId, Long targetId, AttachmentTargetType target);
}

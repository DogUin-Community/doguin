package com.sparta.doguin.domain.attachment.repository;

import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;
import com.sparta.doguin.domain.attachment.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    /**
     * @title 유저아이디,타겟아이디,타겟이 일치하는 파일의 파일경로 전체 반환 쿼리
     *
     * @param userId 유저아이디
     * @param targetId 타겟아이디
     * @param target 터갯
     * @return 위 3가지가 일치하는 파일의 경로들 전부 반환
     * @since 1.0
     * @author 1.0
     */
    @Query("SELECT a.attachment_absolute_path from Attachment a " +
            "WHERE a.user.id = :userId " +
            "AND a.targetId = :targetId " +
            "AND a.target = :target "
    )
    List<String> findAllAttachmentPathByUserIdAndTagertIdAndTarget(Long userId, Long targetId, AttachmentTargetType target);

    /**
     * @title 타겟아이디, 타겟이 일치하는 파일의 파일경로 반환
     *
     * @param targetId 타겟아이디
     * @param target 타겟
     * @return 위 2가지가 일치하는 파일의
     * @since 1.0
     * @author 김경민
     */
    @Query("SELECT a.attachment_absolute_path from Attachment a " +
            "WHERE a.targetId = :targetId " +
            "AND a.target = :target "
    )
    List<String> findAllAttachmentPathByTagertIdAndTarget(Long targetId, AttachmentTargetType target);

    /**
     * @title 유저아이디,타겟아이디,타겟이 일치하는 파일의 아이디 전체 반환 쿼리
     *
     * @param userId 유저아이디
     * @param targetId 타겟아이디
     * @param target 터갯
     * @return 위 3가지가 일치하는 파일의 경로들 전부 반환
     * @since 1.0
     * @author 1.0
     */
    @Query("SELECT a.id from Attachment a " +
            "WHERE a.user.id = :userId " +
            "AND a.targetId = :targetId " +
            "AND a.target = :target "
    )
    List<Long> findAllAttachmentIdByUserIdAndTagertIdAndTarget(Long userId, Long targetId, AttachmentTargetType target);

    /**
     * @title 파일 아이디들로 파일찾고, 파일 반환
     *
     * @param attachmentIds 파일 아이디들
     * @return 위 조건에 맞는 파일 반환
     * @since 1.0
     * @author 김경민
     */
    @Query("SELECT a from Attachment a " +
            "WHERE a.id IN :attachmentIds "
    )
    List<Attachment> findAllByAttachment(List<Long> attachmentIds);

    /**
     * @title 파일 아이디들로 파일찾고, 해당 파일들의 절대경로 반환 쿼리
     *
     * @param attachmentIds 파일 아이디들
     * @return 위 조건에 맞는 파일의 경로 반환
     * @since 1.0
     * @author 김경민
     */
    @Query("SELECT a.attachment_absolute_path from Attachment a " +
            "WHERE a.id IN :attachmentIds "
    )
    List<String> findAllByAttachmentPath(List<Long> attachmentIds);

    /**
     * @title 타겟아이디, 타겟이 일치하는 파일의 아이디 전체 반환 쿼리
     *
     * @param targetId 타겟아이디
     * @param target 타겟
     * @return 위 조건에 맞는 파일의 아이디 목록 반환
     * @since 1.0
     */
    @Query("SELECT a.id from Attachment a " +
            "WHERE a.targetId = :targetId " +
            "AND a.target = :target")
    List<Long> findAllAttachmentIdByTagertIdAndTarget(Long targetId, AttachmentTargetType target);

}

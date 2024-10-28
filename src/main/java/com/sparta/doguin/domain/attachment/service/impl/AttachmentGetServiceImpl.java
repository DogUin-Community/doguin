package com.sparta.doguin.domain.attachment.service.impl;

import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;
import com.sparta.doguin.domain.attachment.entity.Attachment;
import com.sparta.doguin.domain.attachment.repository.AttachmentRepository;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttachmentGetServiceImpl implements AttachmentGetService {
    private final AttachmentRepository attachmentRepository;

    /**
     * @title 타겟에게 달려있는 파일들의 파일경로 반환
     * @description 유저 ID, 타겟 ID, 타겟 으로 특정 타겟 엔티티에 첨부되어있는 파일들의 파일경로를 가져옵니다
     *
     * @param userId 유저 ID
     * @param targetId 타겟 ID
     * @param target 타겟
     * @return 위 3가지의 조건에 맞는 파일의 파일경로를 전부 반환합니다
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    @Override
    public List<String> getAllAttachmentPath(Long userId, Long targetId, AttachmentTargetType target) {
        return attachmentRepository.findAllAttachmentPathByUserIdAndTagertIdAndTarget(userId,targetId,target);
    }

    /**
     * @title 타겟에게 달려있는 파일들의 파일경로 반환
     * @description 타겟 ID, 타겟 으로
     *
     * @param targetId 타겟 ID
     * @param target 타겟
     * @return 위 2가지의 조건에 맞는 파일의 파일경로를 전부 반환합니다
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    @Override
    public List<String> getAllAttachmentPath(Long targetId, AttachmentTargetType target) {
        return attachmentRepository.findAllAttachmentPathByTagertIdAndTarget(targetId,target);
    }

    /**
     * @title 파일 아이디에 해당하는 경로 전부반환
     *
     * @param attachmentIds 파일 아이디들
     * @return 파일 아이디에 해당하는 파일 경로들 반환
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    @Override
    public List<String> getAllAttachmentPath(List<Long> attachmentIds) {
        return attachmentRepository.findAllByAttachmentPath(attachmentIds);
    }

    /**
     * @title 타겟 생성시, 타겟의 ID 반환 메서드
     * @description 타겟 생성시, 생성된 타겟의 ID를 찾고 반환하는 메서드
     *
     * @param userId 유저 ID
     * @param targetId 타겟 ID
     * @param target 타겟
     * @return 위 3가지의 조건에 맞는 파일 ID 전부 반환
     */
    @Override
    public List<Long> getFileIds(Long userId, Long targetId, AttachmentTargetType target) {
        return attachmentRepository.findAllAttachmentIdByUserIdAndTagertIdAndTarget(userId,targetId,target);
    }

    /**
     * @title 파일 ID의 값들로 파일을 찾고, 파일들을 반환
     *
     * @param attachmentIds 파일 ID들
     * @return 파일 ID에 해당하는 모든 파일을 반환
     * @since 1.0
     * @author 김경민
     */
    public List<Attachment> getFiles(List<Long> attachmentIds) {
        return attachmentRepository.findAllByAttachment(attachmentIds);
    }
}
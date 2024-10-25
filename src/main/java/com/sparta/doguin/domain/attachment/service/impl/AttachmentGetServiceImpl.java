package com.sparta.doguin.domain.attachment.service.impl;

import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;
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
     * 특정 유저의 포트폴리오 조회 및 첨부된 파일들 조회시, 파일의 첨부경로들 반환하는 메서드
     *
     * @param userId
     * @param targetId
     * @param targetType
     * @return
     */
    @Transactional
    @Override
    public List<String> getFilePath(Long userId, Long targetId, AttachmentTargetType targetType) {
        return attachmentRepository.findAllFilePathByUserIdAndTagertIdAndTarget(userId,targetId,targetType);
    }

    /**
     * 모두의 포트폴리오 조회 및 첨부된 파일들 조회시, 파일의 첨부경로들 반환하는 메서드
     *
     * @param targetId
     * @param targetType
     * @return
     */
    @Transactional
    @Override
    public List<String> getAllFilePath(Long targetId, AttachmentTargetType targetType) {
        return attachmentRepository.findAllFilePathByTagertIdAndTarget(targetId,targetType);
    }

    /**
     * 포트폴리오 생성시, 포트폴리오에 첨부된 파일들의 아이디 반환 메서드
     *
     * @param userId
     * @param targetId
     * @param targetType
     * @return
     */
    @Override
    public List<Long> getFileIds(Long userId, Long targetId, AttachmentTargetType targetType) {
        return attachmentRepository.findAllFileIdByUserIdAndTagertIdAndTarget(userId,targetId,targetType);
    }

}
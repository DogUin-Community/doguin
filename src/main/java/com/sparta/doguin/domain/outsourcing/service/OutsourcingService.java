package com.sparta.doguin.domain.outsourcing.service;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingRequest;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingResponse;
import com.sparta.doguin.security.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OutsourcingService {
    OutsourcingResponse getOutsourcing(Long outsourcingId);
    OutsourcingResponse createOutsourcing(OutsourcingRequest.OutsourcingRequestCreate reqDto, AuthUser authUser, List<MultipartFile> files);
    void updateOutsourcing(Long outsourcingId, OutsourcingRequest.OutsourcingRequestUpdate reqDto, AuthUser authUser,List<MultipartFile> files);
    void deleteOutsourcing(Long outsourcingId,AuthUser authUser);
    Page<OutsourcingResponse> getAllOutsourcing(Pageable pageable, AreaType area);
    Page<OutsourcingResponse> search(Pageable pageable, String title, String nickname, String content);
}

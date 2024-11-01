package com.sparta.doguin.domain.outsourcing.service;

import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingRequest;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OutsourcingService {
    ApiResponse<OutsourcingResponse> getOutsourcing(Long outsourcingId);
    ApiResponse<OutsourcingResponse> createOutsourcing(OutsourcingRequest.OutsourcingRequestCreate reqDto, AuthUser authUser, List<MultipartFile> files);
    ApiResponse<Void> updateOutsourcing(Long outsourcingId, OutsourcingRequest.OutsourcingRequestUpdate reqDto, AuthUser authUser,List<MultipartFile> files);
    ApiResponse<Void> deleteOutsourcing(Long outsourcingId,AuthUser authUser);
    ApiResponse<Page<OutsourcingResponse>> getAllOutsourcing(Pageable pageable, AreaType area);
    ApiResponse<Page<OutsourcingResponse>> search(Pageable pageable, String title, String nickname, String content);
}

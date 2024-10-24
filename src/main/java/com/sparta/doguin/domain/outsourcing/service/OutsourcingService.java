package com.sparta.doguin.domain.outsourcing.service;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingRequest;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OutsourcingService {
    ApiResponse<OutsourcingResponse> getOutsourcing(Long outsourcingId);
    ApiResponse<Void> createOutsourcing(OutsourcingRequest.OutsourcingRequestCreate reqDto, AuthUser authUser);
    ApiResponse<Void> updateOutsourcing(Long outsourcingId, OutsourcingRequest.OutsourcingRequestUpdate reqDto, AuthUser authUser);
    ApiResponse<Void> deleteOutsourcing(Long outsourcingId,AuthUser authUser);
    ApiResponse<Page<OutsourcingResponse>> getAllOutsourcing(Pageable pageable, AreaType area);
}

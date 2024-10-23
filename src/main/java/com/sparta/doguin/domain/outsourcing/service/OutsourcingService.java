package com.sparta.doguin.domain.outsourcing.service;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.outsourcing.model.OutsourctingRequest;
import com.sparta.doguin.domain.outsourcing.model.OutsourctingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OutsourcingService {
    ApiResponse<OutsourctingResponse> getOutsourcing(Long outsourcingId);
    ApiResponse<Void> createOutsourcing(OutsourctingRequest.OutsourcingRequestCreate reqDto, AuthUser authUser);
    ApiResponse<Void> updateOutsourcing(Long outsourcingId, OutsourctingRequest.OutsourcingRequestUpdate reqDto,AuthUser authUser);
    ApiResponse<Void> deleteOutsourcing(Long outsourcingId,AuthUser authUser);
    ApiResponse<Page<OutsourctingResponse>> getAllOutsourcing(Pageable pageable, AreaType area);
}

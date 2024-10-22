package com.sparta.doguin.domain.outsourcing.service;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingRequestDto;
import com.sparta.doguin.domain.outsourcing.model.OutsourctingResponse;

public interface OutsourcingService {
    ApiResponse<OutsourctingResponse> getOutsourcing(Long outsourcingId);
    ApiResponse<Void> createOutsourcing(OutsourcingRequestDto reqDto);
    ApiResponse<Void> updateOutsourcing(Long outsourcingId, OutsourcingRequestDto reqDto);
    ApiResponse<Void> deleteOutsourcing(Long outsourcingId);
}

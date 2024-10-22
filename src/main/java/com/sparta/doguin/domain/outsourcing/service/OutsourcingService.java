package com.sparta.doguin.domain.outsourcing.service;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.model.OutsourctingDto;

public interface OutsourcingService {
    ApiResponse<OutsourctingDto> getOutsourcing(Long outsourcingId);
    ApiResponse<Void> createOutsourcing(OutsourctingDto.OutsourcingRequest reqDto);
    ApiResponse<Void> updateOutsourcing(Long outsourcingId, OutsourctingDto.OutsourcingRequest reqDto);
    ApiResponse<Void> deleteOutsourcing(Long outsourcingId);
}

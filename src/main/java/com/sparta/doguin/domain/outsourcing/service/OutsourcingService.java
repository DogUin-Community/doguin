package com.sparta.doguin.domain.outsourcing.service;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.outsourcing.model.OutsourctingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OutsourcingService {
    ApiResponse<OutsourctingDto> getOutsourcing(Long outsourcingId);
    ApiResponse<Void> createOutsourcing(OutsourctingDto.OutsourcingRequest reqDto, AuthUser authUser);
    ApiResponse<Void> updateOutsourcing(Long outsourcingId, OutsourctingDto.OutsourcingRequestUpdate reqDto,AuthUser authUser);
    ApiResponse<Void> deleteOutsourcing(Long outsourcingId,AuthUser authUser);
    ApiResponse<Page<OutsourctingDto>> getAllOutsourcing(Pageable pageable, AreaType area);
}

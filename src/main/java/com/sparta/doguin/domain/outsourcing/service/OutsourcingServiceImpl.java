package com.sparta.doguin.domain.outsourcing.service;

import com.sparta.doguin.domain.common.exception.OutsourcingException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseOutsourcingEnum;
import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingRequestDto;
import com.sparta.doguin.domain.outsourcing.model.OutsourctingResponse;
import com.sparta.doguin.domain.outsourcing.repository.OutsourcingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutsourcingServiceImpl implements OutsourcingService {
    private final OutsourcingRepository outsourcingRepository;

    @Override
    public ApiResponse<OutsourctingResponse> getOutsourcing(Long outsourcingId) {
        Outsourcing outsourcing = findById(outsourcingId);
        ApiResponseOutsourcingEnum apiResponseOutsourcingEnum = ApiResponseOutsourcingEnum.OUTSOURCING_SUCCESS;
        OutsourctingResponse outsourctingResponse = OutsourctingResponse.OutsourcingResponseDto.of(outsourcing);
        return ApiResponse.of(apiResponseOutsourcingEnum,outsourctingResponse);
    }

    // TODO: 유저 추가해야함
    @Override
    public ApiResponse<Void> createOutsourcing(OutsourcingRequestDto reqDto) {
        Outsourcing outsourcing = Outsourcing.builder()
                .title(reqDto.getTitle())
                .content(reqDto.getContent())
                .preferential(reqDto.getPreferential())
                .work_type(reqDto.getWork_type())
                .price(reqDto.getPrice())
                .recruit_start_date(reqDto.getRecruit_start_date())
                .recruit_end_date(reqDto.getRecruit_end_date())
                .work_start_date(reqDto.getWork_start_date())
                .work_end_date(reqDto.getWork_end_date())
                .area(reqDto.getArea())
                .build();
        outsourcingRepository.save(outsourcing);
        return ApiResponse.of(ApiResponseOutsourcingEnum.OUTSOURCING_SUCCESS);
    }

    // TODO: 유저 추가해야함
    @Override
    public ApiResponse<Void> updateOutsourcing(Long outsourcingId, OutsourcingRequestDto reqDto) {
        Outsourcing findOutsourcing = findById(outsourcingId);
        Outsourcing updateOutsourcing = Outsourcing.builder()
                .id(findOutsourcing.getId())
                .title(reqDto.getTitle())
                .content(reqDto.getContent())
                .preferential(reqDto.getPreferential())
                .work_type(reqDto.getWork_type())
                .price(reqDto.getPrice())
                .recruit_start_date(reqDto.getRecruit_start_date())
                .recruit_end_date(reqDto.getRecruit_end_date())
                .work_start_date(reqDto.getWork_start_date())
                .work_end_date(reqDto.getWork_end_date())
                .area(reqDto.getArea())
                .build();
        outsourcingRepository.save(updateOutsourcing);
        return ApiResponse.of(ApiResponseOutsourcingEnum.OUTSOURCING_SUCCESS);
    }

    @Override
    public ApiResponse<Void> deleteOutsourcing(Long outsourcingId) {
        Outsourcing outsourcing = findById(outsourcingId);
        outsourcingRepository.delete(outsourcing);
        return ApiResponse.of(ApiResponseOutsourcingEnum.OUTSOURCING_SUCCESS);
    }

    public Outsourcing findById(Long outsourcingId){
        return outsourcingRepository.findById(outsourcingId).orElseThrow(() -> new OutsourcingException(ApiResponseOutsourcingEnum.OUTSOURCING_NOT_FOUND));
    }
}

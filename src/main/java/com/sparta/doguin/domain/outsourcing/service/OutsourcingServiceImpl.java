package com.sparta.doguin.domain.outsourcing.service;

import com.sparta.doguin.domain.common.exception.OutsourcingException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseOutsourcingEnum;
import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
import com.sparta.doguin.domain.outsourcing.model.OutsourctingDto;
import com.sparta.doguin.domain.outsourcing.repository.OutsourcingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutsourcingServiceImpl implements OutsourcingService {
    private final OutsourcingRepository outsourcingRepository;

    @Override
    public ApiResponse<OutsourctingDto> getOutsourcing(Long outsourcingId) {
        Outsourcing outsourcing = findById(outsourcingId);
        ApiResponseOutsourcingEnum apiResponseOutsourcingEnum = ApiResponseOutsourcingEnum.OUTSOURCING_SUCCESS;
        OutsourctingDto outsourctingResponse = OutsourctingDto.OutsourcingResponse.of(outsourcing);
        return ApiResponse.of(apiResponseOutsourcingEnum,outsourctingResponse);
    }

    // TODO: 유저 추가해야함
    @Override
    public ApiResponse<Void> createOutsourcing(OutsourctingDto.OutsourcingRequest reqDto) {
        Outsourcing outsourcing = Outsourcing.builder()
                .title(reqDto.title())
                .content(reqDto.content())
                .preferential(reqDto.preferential())
                .work_type(reqDto.work_type())
                .price(reqDto.price())
                .recruit_start_date(reqDto.recruit_start_date())
                .recruit_end_date(reqDto.recruit_end_date())
                .work_start_date(reqDto.work_start_date())
                .work_end_date(reqDto.work_end_date())
                .area(reqDto.area())
                .build();
        outsourcingRepository.save(outsourcing);
        return ApiResponse.of(ApiResponseOutsourcingEnum.OUTSOURCING_SUCCESS);
    }

    // TODO: 유저 추가해야함
    @Override
    public ApiResponse<Void> updateOutsourcing(Long outsourcingId, OutsourctingDto.OutsourcingRequest reqDto) {
        Outsourcing findOutsourcing = findById(outsourcingId);
        Outsourcing updateOutsourcing = Outsourcing.builder()
                .id(findOutsourcing.getId())
                .title(reqDto.title())
                .content(reqDto.content())
                .preferential(reqDto.preferential())
                .work_type(reqDto.work_type())
                .price(reqDto.price())
                .recruit_start_date(reqDto.recruit_start_date())
                .recruit_end_date(reqDto.recruit_end_date())
                .work_start_date(reqDto.work_start_date())
                .work_end_date(reqDto.work_end_date())
                .area(reqDto.area())
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

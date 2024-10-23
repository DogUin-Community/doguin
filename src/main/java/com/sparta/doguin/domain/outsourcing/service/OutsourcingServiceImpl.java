package com.sparta.doguin.domain.outsourcing.service;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.common.exception.OutsourcingException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
import com.sparta.doguin.domain.outsourcing.model.OutsourctingDto;
import com.sparta.doguin.domain.outsourcing.repository.OutsourcingRepository;
import com.sparta.doguin.domain.outsourcing.validate.OutsourcingValidator;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.doguin.domain.common.response.ApiResponseOutsourcingEnum.OUTSOURCING_NOT_FOUND;
import static com.sparta.doguin.domain.common.response.ApiResponseOutsourcingEnum.OUTSOURCING_SUCCESS;

@Service
@RequiredArgsConstructor
public class OutsourcingServiceImpl implements OutsourcingService {
    private final OutsourcingRepository outsourcingRepository;

    // TODO: 바꿔야함
    private final UserRepository userRepository;

    /**
     * 외주 ID로 외주 데이터 반환 하는 메서드
     *
     * @param outsourcingId / 찾을 아웃소싱 ID
     * @return ApiResponse<OutsourctingDto> / 찾은 외주 데이터 반환
     * @throws OutsourcingException / 외주가 존재하지않을때 발생되는 예외
     * @since  1.0
     * @author 김경민
     */
    @Transactional(readOnly = true)
    @Override
    public ApiResponse<OutsourctingDto> getOutsourcing(Long outsourcingId) {
        Outsourcing outsourcing = findById(outsourcingId);
        OutsourctingDto outsourctingResponse = OutsourctingDto.OutsourcingResponse.of(outsourcing);
        return ApiResponse.of(OUTSOURCING_SUCCESS,outsourctingResponse);
    }

    /**
     * 외주 생성 메서드
     *
     * @param reqDto / 외주 생성 데이터
     * @param authUser / 외주 생성할 유저
     * @return ApiResponse<Void> / 요청 성공 응답 반환
     * @throws OutsourcingException / 외주가 존재하지않을때 발생되는 예외
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    @Override
    public ApiResponse<Void> createOutsourcing(OutsourctingDto.OutsourcingRequest reqDto, AuthUser authUser) {
        User user = userRepository.findById(Long.parseLong(authUser.getUserId())).orElseThrow();
        Outsourcing outsourcing = Outsourcing.builder()
                .user(user)
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
        return ApiResponse.of(OUTSOURCING_SUCCESS);
    }

    /**
     * 외주 수정 메서드
     *
     * @param outsourcingId / 수정할 외주 ID
     * @param reqDto / 수정할 외주 데이터
     * @param authUser / 외주 수정할 유저
     * @return ApiResponse<Void> / 요청 성공 응답 반환
     * @throws OutsourcingException / 외주가 존재하지않을때 발생되는 예외
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    @Override
    public ApiResponse<Void> updateOutsourcing(Long outsourcingId, OutsourctingDto.OutsourcingRequestUpdate reqDto, AuthUser authUser) {
        User user = userRepository.findById(Long.parseLong(authUser.getUserId())).orElseThrow();
        Outsourcing findOutsourcing = findById(outsourcingId);
        OutsourcingValidator.isMe(user.getId(),findOutsourcing.getUser().getId());
        if (String.valueOf(findOutsourcing.getUser().getId()).equals(authUser.getUserId()) ) {
            throw new OutsourcingException(OUTSOURCING_NOT_FOUND);
        }
        Outsourcing updateOutsourcing = Outsourcing.builder()
                .id(findOutsourcing.getId())
                .user(findOutsourcing.getUser())
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
        return ApiResponse.of(OUTSOURCING_SUCCESS);
    }

    /**
     * 외주 삭제 메서드
     *
     * @param outsourcingId / 삭제할 외주 ID
     * @param authUser / 외주 삭제할 유저
     * @return ApiResponse<Void> / 요청 성공 응답 반환
     * @throws OutsourcingException / 외주가 존재하지않을때 발생되는 예외
     * @since 1.0
     * @author 김경민
     */
    @Transactional(readOnly = true)
    @Override
    public ApiResponse<Void> deleteOutsourcing(Long outsourcingId,AuthUser authUser) {
        User user = userRepository.findById(Long.parseLong(authUser.getUserId())).orElseThrow();
        Outsourcing outsourcing = findById(outsourcingId);
        OutsourcingValidator.isMe(user.getId(),outsourcing.getUser().getId());
        outsourcingRepository.delete(outsourcing);
        return ApiResponse.of(OUTSOURCING_SUCCESS);
    }

    /**
     * 외주 전체 조회
     *
     * @param pageable / 전체 조회할 페이지 정보 (페이지,사이즈,정렬여부)
     * @return ApiResponse<Page<OutsourctingDto>> / 조회된 외주 페이지 단위로 반환
     */
    @Override
    public ApiResponse<Page<OutsourctingDto>> getAllOutsourcing(Pageable pageable, AreaType area) {
        Page<Outsourcing> pageableBookmarks;
        if (area == null) {
            pageableBookmarks = outsourcingRepository.findAllBy(pageable);
        } else {
            pageableBookmarks = outsourcingRepository.findAllByArea(pageable,area);
        }

        Page<OutsourctingDto> bookmarks = pageableBookmarks.map(OutsourctingDto.OutsourcingResponse::of);
        return ApiResponse.of(OUTSOURCING_SUCCESS,bookmarks);
    }

    /**
     * ID로 외주 찾는 메서드
     *
     * @param outsourcingId / 외주 찾을 ID
     * @return Outsourcing / 찾은 외주 데이터 반환
     * @throws OutsourcingException / 외주가 존재하지않을때 발생되는 예외
     * @since 1.0
     * @author 김경민
     */
    @Transactional(readOnly = true)
    public Outsourcing findById(Long outsourcingId){
        return outsourcingRepository.findById(outsourcingId).orElseThrow(() -> new OutsourcingException(OUTSOURCING_NOT_FOUND));
    }
}

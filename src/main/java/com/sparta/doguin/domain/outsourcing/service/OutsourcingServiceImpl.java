package com.sparta.doguin.domain.outsourcing.service;

import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentDeleteService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentGetService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUpdateService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUploadService;
import com.sparta.doguin.domain.common.exception.OutsourcingException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingRequest;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingResponse;
import com.sparta.doguin.domain.outsourcing.repository.OutsourcingRepository;
import com.sparta.doguin.domain.outsourcing.validate.OutsourcingValidator;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.notification.slack.SlackSendPush;
import com.sparta.doguin.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.sparta.doguin.domain.attachment.constans.AttachmentTargetType.OUTSOURCING;
import static com.sparta.doguin.domain.common.response.ApiResponseOutsourcingEnum.OUTSOURCING_NOT_FOUND;
import static com.sparta.doguin.domain.common.response.ApiResponseOutsourcingEnum.OUTSOURCING_SUCCESS;

@Service
@RequiredArgsConstructor
public class OutsourcingServiceImpl implements OutsourcingService {
    private final OutsourcingRepository outsourcingRepository;
    private final AttachmentUploadService attachmentUploadService;
    private final AttachmentUpdateService attachmentUpdateService;
    private final AttachmentGetService attachmentGetService;
    private final AttachmentDeleteService attachmentDeleteService;
    private final SlackSendPush slackSendPush;


    /**
     * 외주 ID로 외주 데이터 반환 하는 메서드
     *
     * @param outsourcingId / 찾을 아웃소싱 ID
     * @return ApiResponse<OutsourctingDto> / 찾은 외주 데이터 반환
     * @throws OutsourcingException / 외주가 존재하지않을때 발생되는 예외
     * @since  1.0
     * @author 김경민
     */
    @Cacheable(value = "outsourcingCache",key = "'outsourcingId'+#outsourcingId")
    @Transactional(readOnly = true)
    @Override
    public OutsourcingResponse getOutsourcing(Long outsourcingId) {
        Outsourcing outsourcing = findById(outsourcingId);
        List<String> filePaths = attachmentGetService.getAllAttachmentPath(outsourcingId, OUTSOURCING);
        return OutsourcingResponse.OutsourcingResponseGetFilePaths.of(outsourcing,filePaths);
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
    public ApiResponse<OutsourcingResponse> createOutsourcing(OutsourcingRequest.OutsourcingRequestCreate reqDto, AuthUser authUser, List<MultipartFile> files) {
        OutsourcingValidator.isNotCompany(authUser);
        User user = User.fromAuthUser(authUser);
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
        Outsourcing saveOutsourcing = outsourcingRepository.save(outsourcing);
        if (files != null) {
            attachmentUploadService.upload(files,authUser,saveOutsourcing.getId(), OUTSOURCING);
            List<Long> fileIds = attachmentGetService.getFileIds(authUser.getUserId(),saveOutsourcing.getId(), OUTSOURCING);
            OutsourcingResponse.OutsourcingResponseGetIds data = OutsourcingResponse.OutsourcingResponseGetIds.of(outsourcing,fileIds);
            return ApiResponse.of(OUTSOURCING_SUCCESS,data);
        }
        OutsourcingResponse.OutsourcingResponseGet data = OutsourcingResponse.OutsourcingResponseGet.of(outsourcing);
        return ApiResponse.of(OUTSOURCING_SUCCESS,data);
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
    @CacheEvict(value = "outsourcingCache",key = "'outsourcingId'+#outsourcingId")
    @Transactional
    @Override
    public ApiResponse<Void> updateOutsourcing(Long outsourcingId, OutsourcingRequest.OutsourcingRequestUpdate reqDto, AuthUser authUser,List<MultipartFile> files) {
        OutsourcingValidator.isNotCompany(authUser);
        User user = User.fromAuthUser(authUser);
        Outsourcing findOutsourcing = findById(outsourcingId);
        OutsourcingValidator.isMe(user.getId(),findOutsourcing.getUser().getId());
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
        if (files != null){
            attachmentUpdateService.update(files,reqDto.fileIds(),authUser);
        }

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
    @Transactional
    @Override
    public ApiResponse<Void> deleteOutsourcing(Long outsourcingId, AuthUser authUser) {
        OutsourcingValidator.isNotCompany(authUser);
        User user = User.fromAuthUser(authUser);
        Outsourcing outsourcing = findById(outsourcingId);
        OutsourcingValidator.isMe(user.getId(),outsourcing.getUser().getId());
        List<Long> fileIds = attachmentGetService.getFileIds(authUser.getUserId(),outsourcing.getId(), OUTSOURCING);
        attachmentDeleteService.delete(authUser,fileIds);
        outsourcingRepository.delete(outsourcing);
        return ApiResponse.of(OUTSOURCING_SUCCESS);
    }

    /**
     * 외주 전체 조회
     *
     * @param pageable / 전체 조회할 페이지 정보 (페이지,사이즈,정렬여부)
     * @return ApiResponse<Page<OutsourctingDto>> / 조회된 외주 페이지 단위로 반환
     * @since 1.0
     * @author 김경민
     */
    @Cacheable(value = "outsourcingCache",key = "'outsourcingPage'+#pageable.pageNumber")
    @Transactional(readOnly = true)
    @Override
    public Page<OutsourcingResponse> getAllOutsourcing(Pageable pageable, AreaType area) {
        Page<Outsourcing> pageOutsourcing;
        if (area == null) {
            pageOutsourcing = outsourcingRepository.findAllBy(pageable);
        } else {
            pageOutsourcing = outsourcingRepository.findAllByArea(pageable,area);
        }

        return pageOutsourcing.map(po -> {
            List<String> filePaths = attachmentGetService.getAllAttachmentPath(po.getId(), OUTSOURCING);
            return OutsourcingResponse.OutsourcingResponseGetFilePaths.of(po,filePaths);
        });
    }

    /**
     * 제목,본문,닉네임 의 데이터가 들어온것에 맞게 외주 데이터 반환 메서드
     *
     * @param pageable 조회할 페이지 정보 (페이지,사이즈,정렬여부)
     * @param title 검색할 제목
     * @param nickname 검색할 닉네임
     * @param content 검색할 본문
     * @return 검색결과에 맞는 데이터 반환
     * @since 1.0
     * @author 김경민
     */
    @Cacheable(value = "outsourcingCache",key = "'outsourcingPage'+#pageable.pageNumber")
    @Transactional(readOnly = true)
    @Override
    public Page<OutsourcingResponse> search(Pageable pageable, String title, String nickname, String content) {
        Page<Outsourcing> search = outsourcingRepository.search(title, content, nickname, pageable);
        return search.map(OutsourcingResponse.OutsourcingResponseGet::of);
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

package com.sparta.doguin.domain.matching.service;

import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.common.exception.MatchingException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseMatchingEnum;
import com.sparta.doguin.domain.matching.constans.MathingStatusType;
import com.sparta.doguin.domain.matching.entity.Matching;
import com.sparta.doguin.domain.matching.model.MatchingRequest;
import com.sparta.doguin.domain.matching.model.MatchingResponse;
import com.sparta.doguin.domain.matching.repository.MatchingRepository;
import com.sparta.doguin.domain.matching.validator.MatchingValidator;
import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
import com.sparta.doguin.domain.outsourcing.service.OutsourcingServiceImpl;
import com.sparta.doguin.domain.portfolio.entity.Portfolio;
import com.sparta.doguin.domain.portfolio.service.PortfolioServiceImpl;
import com.sparta.doguin.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.doguin.domain.common.response.ApiResponseMatchingEnum.MATHCING_SUCCESS;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {
    private final MatchingRepository matchingRepository;
    private final OutsourcingServiceImpl outsourcingService;
    private final PortfolioServiceImpl portfolioService;


    /**
     * 매칭생성 메서드 (유저,포트폴리오,외주 3개로 확인함)
     *
     * @param reqDto / 매칭 생성할 데이터
     * @param authUser / 매칭할 유저
     * @return ApiResponse<Void> / 성긍 응답 반환
     * @throws MatchingException / 매칭 찾지 못할때 발생되는 예외
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    @Override
    public ApiResponse<Void> createMatching(MatchingRequest.MatchingRequestCreate reqDto, AuthUser authUser) {
        User user = User.fromAuthUser(authUser);
        Outsourcing outsourcing = outsourcingService.findById(reqDto.outsourcingId());
        Portfolio portfolio = portfolioService.findById(reqDto.portfolioId());
        Matching matching = Matching.builder()
                .user(user)
                .portfolio(portfolio)
                .outsourcing(outsourcing)
                .status(MathingStatusType.READY)
                .build();
        matchingRepository.save(matching);
        return ApiResponse.of(MATHCING_SUCCESS);
    }

    /**
     * 매칭 상태 수정 (준비,완료,거절)
     *
     * @param matchingId / 수정할 매칭 ID
     * @param updateReqDto / 수정할 상태 데이터
     * @return ApiResponse<Void> / 성공 응답 반환
     * @throws MatchingException / 매칭 찾지 못할때 발생되는 예외
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    @Override
    public ApiResponse<Void> updateMatching(Long matchingId, MatchingRequest.MatchingRequestUpdate updateReqDto, AuthUser authUser) {
        User user = User.fromAuthUser(authUser);
        Matching findMatching = findById(matchingId);
        MatchingValidator.isMe(user.getId(),findMatching.getUser().getId());
        Matching updateMatching = Matching.builder()
                .id(findMatching.getId())
                .user(findMatching.getUser())
                .portfolio(findMatching.getPortfolio())
                .outsourcing(findMatching.getOutsourcing())
                .status(updateReqDto.status())
                .build();
        matchingRepository.save(updateMatching);
        return ApiResponse.of(MATHCING_SUCCESS);
    }

    /**
     * 매칭 삭제
     *
     * @param matchingId / 삭제할 매칭 ID
     * @return ApiResponse<Void> / 성공 응답 반환
     * @throws MatchingException / 매칭 찾지 못할때 발생되는 예외
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    @Override
    public ApiResponse<Void> deleteMatching(Long matchingId, AuthUser authUser) {
        User user = User.fromAuthUser(authUser);
        Matching matching = findById(matchingId);
        MatchingValidator.isMe(user.getId(),matching.getUser().getId());
        matchingRepository.delete(matching);
        return ApiResponse.of(MATHCING_SUCCESS);
    }

    /**
     * 자신의 매칭 여부들 확인 가능
     * status 값으로 수락,거절로 조회도 가능합니다
     * status 값이 null이라면 모든 매칭을 반환합니다
     *
     * @param authUser / 로그인 된 유저
     * @param pageable / 패이지 데이터
     * @param status / 준비,완료,거절
     * @return ApiResponse<Page<MatchingDto>> / status값에 맞는 매칭 데이터 반환
     * @since 1.0
     * @author 김경민
     */
    @Transactional(readOnly = true)
    @Override
    public ApiResponse<Page<MatchingResponse>> getAllMatching(AuthUser authUser, Pageable pageable, MathingStatusType status) {
        User user = User.fromAuthUser(authUser);
        Page<Matching> pageableMatchings;
        if (status == null) {
            pageableMatchings = matchingRepository.findAllByUser(user, pageable);
        } else {
            pageableMatchings = matchingRepository.findAllByUserAndStatus(user,pageable,status);
        }

        Page<MatchingResponse> matchings = pageableMatchings.map(MatchingResponse.MatchingResponseGet::of);
        return ApiResponse.of(MATHCING_SUCCESS,matchings);
    }

    @Transactional(readOnly = true)
    public Matching findById(Long matchingId){
        return matchingRepository.findById(matchingId).orElseThrow(() -> new MatchingException(ApiResponseMatchingEnum.MATCHING_NOT_FOUND));
    }
}

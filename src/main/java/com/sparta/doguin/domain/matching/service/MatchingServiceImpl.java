package com.sparta.doguin.domain.matching.service;

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
import com.sparta.doguin.domain.user.enums.UserType;
import com.sparta.doguin.security.AuthUser;
import io.micrometer.core.annotation.Counted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.sparta.doguin.domain.common.response.ApiResponseMatchingEnum.*;

@Slf4j
@Service
public class MatchingServiceImpl implements MatchingService {
    private final MatchingRepository matchingRepository;
    private final OutsourcingServiceImpl outsourcingService;
    private final PortfolioServiceImpl portfolioService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor(); // 단일 스레드 풀로 요청을 순차 처리합니다.
    private static final String QUEUE_KEY = "toggleMatchingQueue";

    public MatchingServiceImpl(MatchingRepository matchingRepository, OutsourcingServiceImpl outsourcingService, PortfolioServiceImpl portfolioService, RedisTemplate<String, Object> redisTemplate) {
        this.matchingRepository = matchingRepository;
        this.outsourcingService = outsourcingService;
        this.portfolioService = portfolioService;
        this.redisTemplate = redisTemplate;
        startProcessingQueue();
    }

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
    @Override
    public ApiResponse<Void> toggleMatching(MatchingRequest.MatchingRequestCreate reqDto, AuthUser authUser) {
        MatchingValidator.isIndividual(authUser);
        Outsourcing outsourcing = outsourcingService.findById(reqDto.outsourcingId());
        Portfolio portfolio = portfolioService.findById(reqDto.portfolioId());
        String requestId = authUser.getUserId() + ":" + outsourcing.getId() + ":" + portfolio.getId();
        redisTemplate.opsForList().leftPush(QUEUE_KEY, requestId);
        return ApiResponse.of(MATHCING_SUCCESS);
    }

    /**
     * 매칭 상태 수정 (준비,완료,거절)
     * 회사만 할 수 있다고 가정
     * 외주를 만든사람만이 매칭 수정 가능
     * @param matchingId / 수정할 매칭 ID
     * @param updateReqDto / 수정할 상태 데이터
     * @return ApiResponse<Void> / 성공 응답 반환
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    @Override
    public ApiResponse<Void> updateMatching(Long matchingId, MatchingRequest.MatchingRequestUpdate updateReqDto, AuthUser authUser) {
        try {
            MatchingValidator.isCompany(authUser);
            Matching findMatching = findById(matchingId);
            Outsourcing outsourcing = outsourcingService.findById(findMatching.getOutsourcing().getId());
            MatchingValidator.isMe(outsourcing.getUser().getId(),authUser.getUserId());
            findMatching.statusChange(updateReqDto.status());
            matchingRepository.flush();
            return ApiResponse.of(MATHCING_SUCCESS);
        } catch (OptimisticLockingFailureException e) {
            throw new MatchingException(MATCHING_LOCK);
        }
    }

    /**
     * 매칭 삭제
     *
     * @param matchingId / 삭제할 매칭 ID
     * 외주를 만든 사람만이, 매칭에 대해 삭제 할 수 있음
     * @return ApiResponse<Void> / 성공 응답 반환
     * @throws MatchingException / 매칭 찾지 못할때 발생되는 예외
     * @since 1.0
     * @author 김경민
     */
    @Transactional
    @Override
    public ApiResponse<Void> deleteMatching(Long matchingId, AuthUser authUser) {
        MatchingValidator.isCompany(authUser);
        Matching matching = findById(matchingId);
        Outsourcing outsourcing = outsourcingService.findById(matching.getOutsourcing().getId());
        MatchingValidator.isMe(outsourcing.getUser().getId(),authUser.getUserId());
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
        Page<Matching> pageableMatchings = null;
        if (authUser.getUserType() == UserType.COMPANY && status == null) {
            pageableMatchings = matchingRepository.findAllByCompanyId(user.getId(), pageable);
        } else if (authUser.getUserType() == UserType.COMPANY && status != null){
            pageableMatchings = matchingRepository.findAllByCompanyIdAndStatus(user.getId(), pageable, status);
        }
        if (authUser.getUserType() == UserType.INDIVIDUAL && status == null) {
            pageableMatchings = matchingRepository.findAllByUser(user, pageable);
        } else if (authUser.getUserType() == UserType.INDIVIDUAL && status != null){
            pageableMatchings = matchingRepository.findAllByUserAndStatus(user,pageable,status);
        }

        Page<MatchingResponse> matchings = pageableMatchings.map(MatchingResponse.MatchingResponseGet::of);
        return ApiResponse.of(MATHCING_SUCCESS,matchings);
    }

    // TEST
    @Transactional(readOnly = true)
    public Matching findById(Long matchingId){
        return matchingRepository.findById(matchingId).orElseThrow(() -> new MatchingException(ApiResponseMatchingEnum.MATCHING_NOT_FOUND));
    }

    private void startProcessingQueue() {
        executorService.submit(() -> {
            while (true) {
                String requestId = (String) redisTemplate.opsForList().rightPop(QUEUE_KEY);
                if (requestId != null) {
                    processMatching(requestId);
                }
            }
        });
    }

    private void processMatching(String requestId) {
        String[] parts = requestId.split(":");
        Long userId = Long.valueOf(parts[0]);
        Long outsourcingId = Long.valueOf(parts[1]);
        Long portfolioId = Long.valueOf(parts[2]);
        Outsourcing outsourcing = outsourcingService.findById(outsourcingId);

        // 기존 toggleMatching 로직을 여기에 넣어줍니다.
        // 예시: matchingRepository.findByUserIdAndOutsourcingIdAndPortfolioId 등을 이용하여 로직 수행
        Optional<Matching> findMatching = matchingRepository.findByUserIdAndOutsourcingIdAndPortfolioId(userId, outsourcingId, portfolioId);
        if (findMatching.isPresent()) {
            matchingRepository.delete(findMatching.get());
        } else {
            Matching matching = Matching.builder()
                    .user(User.builder().id(userId).build()) // 필요한 유저 객체 생성
                    .companyId(outsourcing.getUser().getId())
                    .outsourcing(Outsourcing.builder().id(outsourcingId).build()) // 필요한 아웃소싱 객체 생성
                    .portfolio(Portfolio.builder().id(portfolioId).build()) // 필요한 포트폴리오 객체 생성
                    .status(MathingStatusType.READY)
                    .build();
            matchingRepository.save(matching);
        }
    }
}

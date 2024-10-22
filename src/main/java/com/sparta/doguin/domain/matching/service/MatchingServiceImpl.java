package com.sparta.doguin.domain.matching.service;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.common.exception.OutsourcingException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseMatchingEnum;
import com.sparta.doguin.domain.matching.constans.MathingStatusType;
import com.sparta.doguin.domain.matching.entity.Matching;
import com.sparta.doguin.domain.matching.model.MatchingDto;
import com.sparta.doguin.domain.matching.repository.MatchingRepository;
import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
import com.sparta.doguin.domain.outsourcing.service.OutsourcingServiceImpl;
import com.sparta.doguin.domain.portfolio.entity.Portfolio;
import com.sparta.doguin.domain.portfolio.service.PortfolioServiceImpl;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {
    private final MatchingRepository matchingRepository;
    private final OutsourcingServiceImpl outsourcingService;
    private final PortfolioServiceImpl portfolioService;

    // TODO: 이거 나중에 뺴야함
    private final UserRepository userRepository;

    @Override
    public ApiResponse<MatchingDto> getMatching(Long matchingId) {
        Matching matching = findById(matchingId);
        ApiResponseMatchingEnum apiResponseOutsourcingEnum = ApiResponseMatchingEnum.MATHCING_SUCCESS;
        MatchingDto matchingDto = MatchingDto.MatchingResponse.of(matching);
        return ApiResponse.of(apiResponseOutsourcingEnum,matchingDto);
    }

    // TODO: 유저 찾는 메서드 변경해야함
    @Override
    public ApiResponse<Void> createMatching(MatchingDto.MatchingRequest reqDto, AuthUser authUser) {
        User user = userRepository.findById(Long.parseLong(authUser.getUserId())).orElseThrow();
        Outsourcing outsourcing = outsourcingService.findById(reqDto.outsourcingId());
        Portfolio portfolio = portfolioService.findById(reqDto.portfolioId());
        Matching matching = Matching.builder()
                .user(user)
                .portfolio(portfolio)
                .outsourcing(outsourcing)
                .status(MathingStatusType.READY)
                .build();
        matchingRepository.save(matching);
        return ApiResponse.of(ApiResponseMatchingEnum.MATHCING_SUCCESS);
    }

    @Override
    public ApiResponse<Void> updateMatching(Long matchingId, MatchingDto.MatchingRequestUpdate updateReqDto) {
        Matching findMatching = findById(matchingId);
        Matching updateMatching = Matching.builder()
                .id(findMatching.getId())
                .user(findMatching.getUser())
                .portfolio(findMatching.getPortfolio())
                .outsourcing(findMatching.getOutsourcing())
                .status(updateReqDto.status())
                .build();
        matchingRepository.save(updateMatching);
        return ApiResponse.of(ApiResponseMatchingEnum.MATHCING_SUCCESS);
    }

    @Override
    public ApiResponse<Void> deleteMatching(Long matchingId) {
        Matching matching = findById(matchingId);
        matchingRepository.delete(matching);
        return ApiResponse.of(ApiResponseMatchingEnum.MATHCING_SUCCESS);
    }

    public Matching findById(Long matchingId){
        return matchingRepository.findById(matchingId).orElseThrow(() -> new OutsourcingException(ApiResponseMatchingEnum.MATCHING_NOT_FOUND));
    }
}

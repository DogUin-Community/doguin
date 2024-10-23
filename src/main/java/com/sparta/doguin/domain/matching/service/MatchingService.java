package com.sparta.doguin.domain.matching.service;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.matching.constans.MathingStatusType;
import com.sparta.doguin.domain.matching.model.MatchingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatchingService {
    ApiResponse<Void> createMatching(MatchingDto.MatchingRequest reqDto, AuthUser authUser);
    ApiResponse<Void> updateMatching(Long matchingId, MatchingDto.MatchingRequestUpdate updateReqDto, AuthUser authUser);
    ApiResponse<Void> deleteMatching(Long matchingId, AuthUser authUser);
    ApiResponse<Page<MatchingDto>> getAllMatching(AuthUser authUser, Pageable pageable, MathingStatusType status);
}

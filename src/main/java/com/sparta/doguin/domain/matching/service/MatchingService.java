package com.sparta.doguin.domain.matching.service;

import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.matching.constans.MathingStatusType;
import com.sparta.doguin.domain.matching.model.MatchingRequest;
import com.sparta.doguin.domain.matching.model.MatchingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatchingService {
    ApiResponse<Void> toggleMatching(MatchingRequest.MatchingRequestCreate reqDto, AuthUser authUser);
    ApiResponse<Void> updateMatching(Long matchingId, MatchingRequest.MatchingRequestUpdate updateReqDto, AuthUser authUser);
    ApiResponse<Void> deleteMatching(Long matchingId, AuthUser authUser);
    ApiResponse<Page<MatchingResponse>> getAllMatching(AuthUser authUser, Pageable pageable, MathingStatusType status);
}

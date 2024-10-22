package com.sparta.doguin.domain.matching.service;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.matching.model.MatchingDto;

public interface MatchingService {
    ApiResponse<MatchingDto> getMatching(Long matchingId);
    ApiResponse<Void> createMatching(MatchingDto.MatchingRequest reqDto, AuthUser authUser);
    ApiResponse<Void> updateMatching(Long matchingId, MatchingDto.MatchingRequestUpdate updateReqDto);
    ApiResponse<Void> deleteMatching(Long matchingId);
}

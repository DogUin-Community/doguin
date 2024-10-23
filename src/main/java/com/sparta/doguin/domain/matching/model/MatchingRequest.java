package com.sparta.doguin.domain.matching.model;

import com.sparta.doguin.domain.matching.constans.MathingStatusType;

public sealed interface MatchingRequest permits MatchingRequest.MatchingRequestCreate, MatchingRequest.MatchingRequestUpdate {
    record MatchingRequestCreate(Long portfolioId,Long outsourcingId) implements MatchingRequest {

    }

    record MatchingRequestUpdate(MathingStatusType status) implements MatchingRequest {

    }
}

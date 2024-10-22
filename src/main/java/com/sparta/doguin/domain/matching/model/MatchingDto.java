package com.sparta.doguin.domain.matching.model;

import com.sparta.doguin.domain.matching.constans.MathingStatusType;
import com.sparta.doguin.domain.matching.entity.Matching;

public sealed interface MatchingDto permits MatchingDto.MatchingResponse, MatchingDto.MatchingRequest, MatchingDto.MatchingRequestUpdate {
    record MatchingRequest(Long portfolioId,Long outsourcingId) implements MatchingDto {

    }

    record MatchingRequestUpdate(MathingStatusType status) implements MatchingDto {

    }

    record MatchingResponse(MathingStatusType status) implements MatchingDto {
        public static MatchingResponse of(Matching matching) {
            return new MatchingResponse(
                    matching.getStatus()
            );
        }
    }
}

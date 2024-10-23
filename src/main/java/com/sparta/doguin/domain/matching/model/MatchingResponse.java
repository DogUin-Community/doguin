package com.sparta.doguin.domain.matching.model;

import com.sparta.doguin.domain.matching.constans.MathingStatusType;
import com.sparta.doguin.domain.matching.entity.Matching;

public sealed interface MatchingResponse permits MatchingResponse.MatchingResponseGet {
    record MatchingResponseGet(Long id,Long userId, Long portfolioId,Long outsourcingId, MathingStatusType status) implements com.sparta.doguin.domain.matching.model.MatchingResponse {
        public static MatchingResponse of(Matching matching) {
            return new MatchingResponseGet(
                    matching.getId(),
                    matching.getUser().getId(),
                    matching.getPortfolio().getId(),
                    matching.getOutsourcing().getId(),
                    matching.getStatus()
            );
        }
    }
}

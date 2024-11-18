package com.sparta.doguin.domain.matching.model;

import com.sparta.doguin.domain.matching.constans.MathingStatusType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public sealed interface MatchingRequest permits MatchingRequest.MatchingRequestCreate, MatchingRequest.MatchingRequestUpdate {
    record MatchingRequestCreate(
            @NotNull(message = "포트폴리오 ID는 공백 일 수 없습니다") @Positive(message = "포트폴리오 ID는 0을 초과 해야 합니다") Long portfolioId,
            @NotNull(message = "외주 ID는 공백 일 수 없습니다") @Positive(message = "외주 ID는 0을 초과 해야 합니다") Long outsourcingId
    ) implements MatchingRequest {

    }

    record MatchingRequestUpdate(
            @NotNull(message = "매칭 상태는 공백 일 수 없습니다") MathingStatusType status
    ) implements MatchingRequest {

    }
}

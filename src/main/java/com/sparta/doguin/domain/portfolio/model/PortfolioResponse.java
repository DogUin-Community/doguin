package com.sparta.doguin.domain.portfolio.model;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;

public sealed interface PortfolioResponse permits PortfolioResponse.PortfolioResponseDto {
    record PortfolioResponseDto(
            Long id,
            Long user_id,
            String title,
            String content,
            Long work_experience,
            String work_type,
            String project_history,
            AreaType area
    ) implements PortfolioResponse {}
}

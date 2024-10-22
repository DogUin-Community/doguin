package com.sparta.doguin.domain.portfolio.model;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.portfolio.entity.Portfolio;

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
    ) implements PortfolioResponse {
        public static PortfolioResponseDto of(Portfolio portfolio) {
            return new PortfolioResponseDto(
                    portfolio.getId(),
                    portfolio.getUser().getId(),
                    portfolio.getTitle(),
                    portfolio.getContent(),
                    portfolio.getWork_experience(),
                    portfolio.getWork_type(),
                    portfolio.getProejct_history(),
                    portfolio.getArea()
            );
        }
    }
}

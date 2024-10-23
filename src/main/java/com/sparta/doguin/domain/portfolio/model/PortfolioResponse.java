package com.sparta.doguin.domain.portfolio.model;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.portfolio.entity.Portfolio;

import java.time.LocalDateTime;

public sealed interface PortfolioResponse permits PortfolioResponse.PortfolioResponseGet {
    record PortfolioResponseGet(
            Long id,
            Long user_id,
            String title,
            String content,
            Long work_experience,
            String work_type,
            String project_history,
            AreaType area,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) implements PortfolioResponse {
        public static PortfolioResponseGet of(Portfolio portfolio) {
            return new PortfolioResponseGet(
                    portfolio.getId(),
                    portfolio.getUser().getId(),
                    portfolio.getTitle(),
                    portfolio.getContent(),
                    portfolio.getWork_experience(),
                    portfolio.getWork_type(),
                    portfolio.getProject_history(),
                    portfolio.getArea(),
                    portfolio.getCreatedAt(),
                    portfolio.getUpdatedAt()
            );
        }
    }
}

package com.sparta.doguin.domain.portfolio.model;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.portfolio.entity.Portfolio;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public sealed interface PortfolioDto permits PortfolioDto.PortfolioResponse,PortfolioDto.PortfolioRequest, PortfolioDto.PortfolioRequestUpdate  {
    record PortfolioResponse(
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
    ) implements PortfolioDto {
        public static PortfolioResponse of(Portfolio portfolio) {
            return new PortfolioResponse(
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
    record PortfolioRequest(
            @NotNull String title,
            @NotNull String content,
            @NotNull Long work_experience,
            @NotNull String work_type,
            @NotNull String proejct_history,
            @NotNull AreaType area
    ) implements PortfolioDto {}

    record PortfolioRequestUpdate(
            String title,
            String content,
            Long work_experience,
            String work_type,
            String proejct_history,
            AreaType area
    ) implements PortfolioDto {}
}

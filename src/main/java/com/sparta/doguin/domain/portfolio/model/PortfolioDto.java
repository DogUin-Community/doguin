package com.sparta.doguin.domain.portfolio.model;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.portfolio.entity.Portfolio;

public sealed interface PortfolioDto permits PortfolioDto.PortfolioResponse,PortfolioDto.PortfolioRequest  {
    record PortfolioResponse(Long id, Long user_id, String title, String content, Long work_experience, String work_type, String project_history, AreaType area) implements PortfolioDto {
        public static PortfolioResponse of(Portfolio portfolio) {
            return new PortfolioResponse(
                    portfolio.getId(),
                    portfolio.getUser().getId(),
                    portfolio.getTitle(),
                    portfolio.getContent(),
                    portfolio.getWork_experience(),
                    portfolio.getWork_type(),
                    portfolio.getProject_history(),
                    portfolio.getArea()
            );
        }
    }
    record PortfolioRequest(String title, String content, Long work_experience, String work_type, String proejct_history, AreaType area) implements PortfolioDto {}
}

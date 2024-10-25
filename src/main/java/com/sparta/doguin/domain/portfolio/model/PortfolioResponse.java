package com.sparta.doguin.domain.portfolio.model;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.portfolio.entity.Portfolio;

import java.time.LocalDateTime;
import java.util.List;

public sealed interface PortfolioResponse permits PortfolioResponse.PortfolioResponseGet,PortfolioResponse.PortfolioResponseGetIds {
    record PortfolioResponseGet(
            Long id,
            Long user_id,
            String title,
            String content,
            Long work_experience,
            String work_type,
            String project_history,
            AreaType area,
            List<String> filePath,
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
                    null,
                    portfolio.getCreatedAt(),
                    portfolio.getUpdatedAt()
            );
        }

        public static PortfolioResponseGet of(Portfolio portfolio, List<String> filePaths) {
            return new PortfolioResponseGet(
                    portfolio.getId(),
                    portfolio.getUser().getId(),
                    portfolio.getTitle(),
                    portfolio.getContent(),
                    portfolio.getWork_experience(),
                    portfolio.getWork_type(),
                    portfolio.getProject_history(),
                    portfolio.getArea(),
                    filePaths,
                    portfolio.getCreatedAt(),
                    portfolio.getUpdatedAt()
            );
        }
    }

    record PortfolioResponseGetIds(
            Long id,
            Long user_id,
            String title,
            String content,
            Long work_experience,
            String work_type,
            String project_history,
            AreaType area,
            List<Long> fileId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) implements PortfolioResponse {
        public static PortfolioResponseGetIds of(Portfolio portfolio, List<Long> fileIds) {
            return new PortfolioResponseGetIds(
                    portfolio.getId(),
                    portfolio.getUser().getId(),
                    portfolio.getTitle(),
                    portfolio.getContent(),
                    portfolio.getWork_experience(),
                    portfolio.getWork_type(),
                    portfolio.getProject_history(),
                    portfolio.getArea(),
                    fileIds,
                    portfolio.getCreatedAt(),
                    portfolio.getUpdatedAt()
            );
        }
    }
}

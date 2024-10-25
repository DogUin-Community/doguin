package com.sparta.doguin.domain.portfolio.model;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public sealed interface PortfolioRequest permits PortfolioRequest.PortfolioRequestCreate, PortfolioRequest.PortfolioRequestUpdate, PortfolioRequest.PortfolioRequestDelete  {
    record PortfolioRequestCreate(
            @NotNull String title,
            @NotNull String content,
            @NotNull Long work_experience,
            @NotNull String work_type,
            @NotNull String proejct_history,
            @NotNull AreaType area
    ) implements PortfolioRequest {}

    record PortfolioRequestUpdate(
            String title,
            String content,
            Long work_experience,
            String work_type,
            String proejct_history,
            AreaType area,
            List<Long> fileIds
    ) implements PortfolioRequest {}

    record PortfolioRequestDelete(
            List<Long> fileIds
    ) implements PortfolioRequest {}
}

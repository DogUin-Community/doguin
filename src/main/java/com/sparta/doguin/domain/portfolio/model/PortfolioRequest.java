package com.sparta.doguin.domain.portfolio.model;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public sealed interface PortfolioRequest permits PortfolioRequest.PortfolioRequestCreate, PortfolioRequest.PortfolioRequestUpdate {
    record PortfolioRequestCreate(
            @NotNull(message = "제목은 공백일 수 없습니다")
            String title,

            @NotNull(message = "내용은 공백일 수 없습니다")
            String content,

            @NotNull(message = "경력은 공백일 수 없습니다")
            Long work_experience,

            @NotNull(message = "근무 형태는 공백일 수 없습니다")
            String work_type,

            @NotNull(message = "프로젝트 이력은 공백일 수 없습니다")
            String project_history,

            @NotNull(message = "지역 정보는 공백일 수 없습니다")
            AreaType area
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
}

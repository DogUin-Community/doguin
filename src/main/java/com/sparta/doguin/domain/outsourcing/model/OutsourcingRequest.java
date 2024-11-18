package com.sparta.doguin.domain.outsourcing.model;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public sealed interface OutsourcingRequest permits OutsourcingRequest.OutsourcingRequestCreate, OutsourcingRequest.OutsourcingRequestUpdate {
    record OutsourcingRequestCreate (
            @NotNull(message = "제목은 공백일 수 없습니다")
            String title,

            @NotNull(message = "내용은 공백일 수 없습니다")
            String content,

            @NotNull(message = "우대사항은 공백일 수 없습니다")
            String preferential,

            @NotNull(message = "근무 형태는 공백일 수 없습니다")
            String work_type,

            @NotNull(message = "가격은 공백일 수 없습니다")
            Long price,

            @NotNull(message = "모집 시작일은 공백일 수 없습니다")
            LocalDateTime recruit_start_date,

            @NotNull(message = "모집 마감일은 공백일 수 없습니다")
            LocalDateTime recruit_end_date,

            @NotNull(message = "작업 시작일은 공백일 수 없습니다")
            LocalDateTime work_start_date,

            @NotNull(message = "작업 마감일은 공백일 수 없습니다")
            LocalDateTime work_end_date,

            @NotNull(message = "지역 정보는 공백일 수 없습니다")
            AreaType area
    ) implements OutsourcingRequest {

    }

    record OutsourcingRequestUpdate(
             String title,
             String content,
             String preferential,
             String work_type,
             Long price,
             LocalDateTime recruit_start_date,
             LocalDateTime recruit_end_date,
             LocalDateTime work_start_date,
             LocalDateTime work_end_date,
             AreaType area,
             List<Long> fileIds
    ) implements OutsourcingRequest {

    }
}

package com.sparta.doguin.domain.outsourcing.model;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public sealed interface OutsourcingRequest permits OutsourcingRequest.OutsourcingRequestCreate, OutsourcingRequest.OutsourcingRequestUpdate {
    record OutsourcingRequestCreate (
            @NotNull String title,
            @NotNull String content,
            @NotNull String preferential,
            @NotNull String work_type,
            @Min(value = 10000) @NotNull Long price,
            @NotNull LocalDateTime recruit_start_date,
            @NotNull LocalDateTime recruit_end_date,
            @NotNull LocalDateTime work_start_date,
            @NotNull LocalDateTime work_end_date,
            @NotNull AreaType area
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
             AreaType area
    ) implements OutsourcingRequest {

    }
}

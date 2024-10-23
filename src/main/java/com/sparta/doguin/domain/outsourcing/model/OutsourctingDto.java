package com.sparta.doguin.domain.outsourcing.model;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public sealed interface OutsourctingDto permits OutsourctingDto.OutsourcingResponse, OutsourctingDto.OutsourcingRequest, OutsourctingDto.OutsourcingRequestUpdate {
    record OutsourcingResponse(
             Long id,
             Long user_id,
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
    ) implements OutsourctingDto {
        public static OutsourcingResponse of(Outsourcing outsourcing) {
            return new OutsourcingResponse(
                    outsourcing.getId(),
                    outsourcing.getUser().getId(),
                    outsourcing.getTitle(),
                    outsourcing.getContent(),
                    outsourcing.getPreferential(),
                    outsourcing.getWork_type(),
                    outsourcing.getPrice(),
                    outsourcing.getRecruit_start_date(),
                    outsourcing.getRecruit_end_date(),
                    outsourcing.getWork_start_date(),
                    outsourcing.getWork_end_date(),
                    outsourcing.getArea()
            );
        }
    }
    record OutsourcingRequest(
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
    ) implements OutsourctingDto {

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
    ) implements OutsourctingDto {

    }
}

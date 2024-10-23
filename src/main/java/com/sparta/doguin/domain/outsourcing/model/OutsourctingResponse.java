package com.sparta.doguin.domain.outsourcing.model;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;

import java.time.LocalDateTime;

public sealed interface OutsourctingResponse permits OutsourctingResponse.OutsourcingResponseGet {
    record OutsourcingResponseGet(
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
             AreaType area,
             LocalDateTime createdAt,
             LocalDateTime updatedAt
    ) implements OutsourctingResponse {
        public static OutsourcingResponseGet of(Outsourcing outsourcing) {
            return new OutsourcingResponseGet(
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
                    outsourcing.getArea(),
                    outsourcing.getCreatedAt(),
                    outsourcing.getUpdatedAt()
            );
        }
    }
}

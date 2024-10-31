package com.sparta.doguin.domain.outsourcing.model;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;

import java.time.LocalDateTime;
import java.util.List;

public sealed interface OutsourcingResponse permits OutsourcingResponse.OutsourcingResponseGet, OutsourcingResponse.OutsourcingResponseGetIds, OutsourcingResponse.OutsourcingResponseGetFilePaths, OutsourcingResponse.OutsourcingResponseGetNoLocalDateTime {
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
    ) implements OutsourcingResponse {
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
    record OutsourcingResponseGetIds(
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
             LocalDateTime updatedAt,
             List<Long> fileIds
    ) implements OutsourcingResponse {
        public static OutsourcingResponseGetIds of(Outsourcing outsourcing, List<Long> fileIds) {
            return new OutsourcingResponseGetIds(
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
                    outsourcing.getUpdatedAt(),
                    fileIds
            );
        }
    }
    record OutsourcingResponseGetFilePaths(
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
            LocalDateTime updatedAt,
            List<String> filePaths
    ) implements OutsourcingResponse {
        public static OutsourcingResponseGetFilePaths of(Outsourcing outsourcing, List<String> filePaths) {
            return new OutsourcingResponseGetFilePaths(
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
                    outsourcing.getUpdatedAt(),
                    filePaths
            );
        }
    }

    record OutsourcingResponseGetNoLocalDateTime(
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
    ) implements OutsourcingResponse {
        public static OutsourcingResponseGetNoLocalDateTime of(Outsourcing outsourcing) {
            return new OutsourcingResponseGetNoLocalDateTime(
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
}

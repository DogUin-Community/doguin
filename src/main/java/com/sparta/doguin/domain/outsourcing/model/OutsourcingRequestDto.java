package com.sparta.doguin.domain.outsourcing.model;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OutsourcingRequestDto {
    private String title;
    private String content;
    // 우대 사항
    private String preferential;

    // 근무 형태
    private String work_type;

    // 임금
    private Long price;

    // 모집 시작일
    private LocalDateTime recruit_start_date;

    // 모집 마감일
    private LocalDateTime recruit_end_date;

    // 근무 시작일
    private LocalDateTime work_start_date;

    // 근무 종료일
    private LocalDateTime work_end_date;

    // 지역
    private AreaType area;

}

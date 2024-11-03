package com.sparta.doguin.domain.outsourcing.entity;

import com.sparta.doguin.domain.common.Timestamped;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

// 외주 엔티티 수정
@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "outsourcing", indexes = {
        @Index(name = "title_content_idx", columnList = "title,content"),
        @Index(name = "title_idx", columnList = "title")
})
@Builder
public class Outsourcing extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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
    @Enumerated(value = EnumType.STRING)
    private AreaType area;

    public Outsourcing(Long id, User user, String title, String content, String preferential, String work_type, Long price, LocalDateTime recruit_start_date, LocalDateTime recruit_end_date, LocalDateTime work_start_date, LocalDateTime work_end_date, AreaType area,LocalDateTime createdAt,LocalDateTime updatedAt) {
        super(createdAt,updatedAt);
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.preferential = preferential;
        this.work_type = work_type;
        this.price = price;
        this.recruit_start_date = recruit_start_date;
        this.recruit_end_date = recruit_end_date;
        this.work_start_date = work_start_date;
        this.work_end_date = work_end_date;
        this.area = area;
    }
}

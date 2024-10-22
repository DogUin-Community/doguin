package com.sparta.doguin.domain.portfolio.entity;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// 외주 엔티티 수정
@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

    private String title;
    private String content;
    // 경력 (연차)
    private Long work_experience;

    // 근무 형태
    private String work_type;

    // 프로젝트 이력
    private String proejct_history;

    // 지역
    @Enumerated(value = EnumType.STRING)
    private AreaType area;
}

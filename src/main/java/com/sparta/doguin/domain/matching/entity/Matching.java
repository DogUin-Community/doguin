package com.sparta.doguin.domain.matching.entity;

import com.sparta.doguin.domain.common.Timestamped;
import com.sparta.doguin.domain.matching.constans.MathingStatusType;
import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
import com.sparta.doguin.domain.portfolio.entity.Portfolio;
import com.sparta.doguin.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

// 외주 엔티티 수정
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Builder
@Entity
public class Matching extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outsourcing_id")
    private Outsourcing outsourcing;

    // 지역
    @Enumerated(value = EnumType.STRING)
    private MathingStatusType status;
}

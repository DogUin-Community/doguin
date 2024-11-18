package com.sparta.doguin.domain.matching.repository;

import com.sparta.doguin.domain.matching.constans.MathingStatusType;
import com.sparta.doguin.domain.matching.entity.Matching;
import com.sparta.doguin.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
    Page<Matching> findAllByUser(User user, Pageable pageable);
    Page<Matching> findAllByUserAndStatus(User user, Pageable pageable, MathingStatusType status);
    // 수정: 지원자 userId와 기업 userId로 매칭 확인
    Optional<Matching> findByUserIdAndOutsourcingUserId(Long userId, Long outsourcingUserId);

    @Query("SELECT m from Matching m " +
            "WHERE m.user.id = :userId " +
            "AND m.outsourcing.id = :outsourcingId " +
            "AND m.portfolio.id = :portfolioId"
    )
    Optional<Matching> findByUserIdAndOutsourcingIdAndPortfolioId(Long userId, Long outsourcingId, Long portfolioId);


    Page<Matching> findAllByCompanyIdAndStatus(Long id, Pageable pageable, MathingStatusType status);

    Page<Matching> findAllByCompanyId(Long id, Pageable pageable);
}

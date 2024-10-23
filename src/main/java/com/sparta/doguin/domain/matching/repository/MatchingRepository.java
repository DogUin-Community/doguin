package com.sparta.doguin.domain.matching.repository;

import com.sparta.doguin.domain.matching.constans.MathingStatusType;
import com.sparta.doguin.domain.matching.entity.Matching;
import com.sparta.doguin.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
    Page<Matching> findAllByUser(User user, Pageable pageable);
    Page<Matching> findAllByUserAndStatus(User user, Pageable pageable, MathingStatusType status);
}

package com.sparta.doguin.domain.matching.repository;

import com.sparta.doguin.domain.matching.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
}

package com.sparta.doguin.domain.portfolio.repository;

import com.sparta.doguin.domain.portfolio.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
}

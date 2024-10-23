package com.sparta.doguin.domain.portfolio.repository;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.portfolio.entity.Portfolio;
import com.sparta.doguin.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Page<Portfolio> findAllByUserAndArea(User user, Pageable pageable, AreaType area);
    Page<Portfolio> findAllByUser(User user, Pageable pageable);
    Page<Portfolio> findAllByArea(Pageable pageable, AreaType area);
    Page<Portfolio> findAllBy(Pageable pageable);

}

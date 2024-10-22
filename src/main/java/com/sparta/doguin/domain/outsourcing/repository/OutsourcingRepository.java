package com.sparta.doguin.domain.outsourcing.repository;

import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutsourcingRepository extends JpaRepository<Outsourcing, Long> {
}

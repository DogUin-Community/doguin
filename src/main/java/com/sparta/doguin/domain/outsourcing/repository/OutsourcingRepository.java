package com.sparta.doguin.domain.outsourcing.repository;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutsourcingRepository extends JpaRepository<Outsourcing, Long>, OutsourcingRepositoryQuery {
    Page<Outsourcing> findAllBy(Pageable pageable);
    Page<Outsourcing> findAllByArea(Pageable pageable, AreaType area);
}

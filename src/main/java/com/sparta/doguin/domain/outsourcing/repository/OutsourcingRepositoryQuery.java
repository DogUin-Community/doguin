package com.sparta.doguin.domain.outsourcing.repository;

import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OutsourcingRepositoryQuery {
    Page<Outsourcing> search(String title, String content, String nickname, Pageable pageable);
}

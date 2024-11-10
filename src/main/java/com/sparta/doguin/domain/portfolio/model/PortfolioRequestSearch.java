package com.sparta.doguin.domain.portfolio.model;

import com.sparta.doguin.domain.common.dto.SearchDto;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioRequestSearch extends SearchDto {

    private AreaType area;
}


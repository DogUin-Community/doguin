package com.sparta.doguin.domain.matching.model;

import com.sparta.doguin.domain.common.dto.SearchDto;
import com.sparta.doguin.domain.matching.constans.MathingStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchingRequestSearch extends SearchDto {

    private MathingStatusType status;
}


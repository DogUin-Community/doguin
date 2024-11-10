package com.sparta.doguin.domain.common.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto {
    @PositiveOrZero(message = "페이지 번호는 0 이상이어야 합니다")
    private Integer page = 0;

    @PositiveOrZero(message = "사이즈는 0 이상이어야 합니다")
    private Integer size = 10;

    @Pattern(regexp = "^(asc|desc)$", message = "정렬 방식은 'asc' 또는 'desc'만 가능합니다")
    private String sort = "desc";
}

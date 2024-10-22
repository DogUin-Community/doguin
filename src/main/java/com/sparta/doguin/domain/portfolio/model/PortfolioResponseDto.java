package com.sparta.doguin.domain.portfolio.model;

import com.sparta.doguin.domain.outsourcing.constans.AreaType;

public final class PortfolioResponseDto {
    private Long id;
    private Long user_id;
    private String title;
    private String content;
    // 경력 (연차)
    private Long work_experience;

    // 근무 형태
    private String work_type;

    // 프로젝트 이력
    private String proejct_history;

    // 지역
    private AreaType area;

//    public PortfolioResponse(Portfolio portfolio) {
//        return new PortfolioResponse(
//                portfolio.getId(),
//                portfolio.getUser().getId(),
//                portfolio.getTitle(),
//                portfolio.getContent(),
//                portfolio.getWork_experience(),
//                portfolio.getWork_type(),
//                portfolio.getProejct_history(),
//                portfolio.getArea()
//        );
//    }


}


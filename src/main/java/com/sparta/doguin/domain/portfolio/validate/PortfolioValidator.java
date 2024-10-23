package com.sparta.doguin.domain.portfolio.validate;

import com.sparta.doguin.domain.common.exception.ValidatorException;

import static com.sparta.doguin.domain.common.response.ApiResponsePortfolioEnum.PROTFOLIO_IS_NOT_ME;

public class PortfolioValidator {
    public static void isMe(Long id, Long targetId) {
        if (!id.equals(targetId) ) {
            throw new ValidatorException(PROTFOLIO_IS_NOT_ME);
        }
    }
}

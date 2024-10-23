package com.sparta.doguin.domain.matching.validator;

import com.sparta.doguin.domain.common.exception.ValidatorException;

import static com.sparta.doguin.domain.common.response.ApiResponseMatchingEnum.MATHCING_IS_NOT_ME;

public class MatchingValidator {
    public static void isMe(Long id, Long targetId) {
        if (!id.equals(targetId) ) {
            throw new ValidatorException(MATHCING_IS_NOT_ME);
        }
    }
}

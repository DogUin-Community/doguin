package com.sparta.doguin.domain.outsourcing.validate;

import com.sparta.doguin.domain.common.exception.ValidatorException;

import static com.sparta.doguin.domain.common.response.ApiResponseOutsourcingEnum.*;

public class OutsourcingValidator {
    public static void isMe(Long id, Long targetId) {
        if (!id.equals(targetId) ) {
            throw new ValidatorException(OUTSOURCING_IS_NOT_ME);
        }
    }
}

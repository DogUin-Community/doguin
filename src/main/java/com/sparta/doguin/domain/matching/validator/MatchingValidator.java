package com.sparta.doguin.domain.matching.validator;

import com.sparta.doguin.domain.common.exception.ValidatorException;
import com.sparta.doguin.domain.user.enums.UserType;
import com.sparta.doguin.security.AuthUser;

import static com.sparta.doguin.domain.common.response.ApiResponseMatchingEnum.*;

public class MatchingValidator {
    public static void isMe(Long id, Long targetId) {
        if (!id.equals(targetId) ) {
            throw new ValidatorException(IS_NOT_ME);
        }
    }

    public static void isNotCompany(AuthUser authUser){
        if (authUser.getUserType() != UserType.COMPANY) {
            throw new ValidatorException(IS_NOT_COMPANY);
        }
    }

    public static void isIndividual(AuthUser authUser){
        if (authUser.getUserType() != UserType.INDIVIDUAL) {
            throw new ValidatorException(IS_NOT_INDIVIDUAL);
        }
    }

}

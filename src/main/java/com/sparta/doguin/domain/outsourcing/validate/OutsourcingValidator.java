package com.sparta.doguin.domain.outsourcing.validate;

import com.sparta.doguin.domain.common.exception.ValidatorException;
import com.sparta.doguin.domain.user.enums.UserType;
import com.sparta.doguin.security.AuthUser;

import static com.sparta.doguin.domain.common.response.ApiResponseMatchingEnum.IS_NOT_COMPANY;
import static com.sparta.doguin.domain.common.response.ApiResponseOutsourcingEnum.*;

public class OutsourcingValidator {
    public static void isMe(Long id, Long targetId) {
        if (!id.equals(targetId) ) {
            throw new ValidatorException(OUTSOURCING_IS_NOT_ME);
        }
    }

    public static void isNotCompany(AuthUser authUser){
        if (authUser.getUserType() != UserType.COMPANY) {
            throw new ValidatorException(IS_NOT_COMPANY);
        }
    }
}

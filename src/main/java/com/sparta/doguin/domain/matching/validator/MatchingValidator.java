package com.sparta.doguin.domain.matching.validator;

import com.sparta.doguin.domain.common.exception.ValidatorException;
import com.sparta.doguin.domain.user.enums.UserType;
import com.sparta.doguin.security.AuthUser;

import static com.sparta.doguin.domain.common.response.ApiResponseMatchingEnum.*;

public class MatchingValidator {
    /**
     * 본인인지 확인하는 메서드
     *
     * @param id 본인 id
     * @param targetId 타겟 id
     */
    public static void isMe(Long id, Long targetId) {
        if (!id.equals(targetId) ) {
            throw new ValidatorException(IS_NOT_ME);
        }
    }

    /**
     * 로그인한 유저가 회사 권한인지 확인
     *
     * @param authUser 로그인한 유저
     */
    public static void isCompany(AuthUser authUser){
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

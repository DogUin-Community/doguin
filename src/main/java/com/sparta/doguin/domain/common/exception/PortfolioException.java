package com.sparta.doguin.domain.common.exception;

import com.sparta.doguin.domain.common.response.ApiResponseEnum;

public class PortfolioException extends BaseException {
    public PortfolioException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }
}

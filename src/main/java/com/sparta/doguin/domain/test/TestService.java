package com.sparta.doguin.domain.test;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseEnum;
import com.sparta.doguin.domain.common.response.ApiResponseTest;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    public ApiResponse<String> test1() {
        String data = "test data";
        ApiResponseEnum apiResponseEnum = ApiResponseTest.TEST_SUCCESS;
        return ApiResponse.of(apiResponseEnum, data);
    }
}

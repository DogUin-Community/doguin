package com.sparta.doguin.domain.test;

import com.sparta.doguin.domain.common.exception.TestException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseTest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {
    private final TestService testService;

    @GetMapping("/one")
    public ResponseEntity<ApiResponse<String>> test1() {
        ApiResponse<String> apiResponse = testService.test1();
        return ApiResponse.of(apiResponse);
    }

    @GetMapping("/one/two")
    public void test2() {
        try {
            throw new Exception("");
        } catch (Exception e) {
            throw new TestException(ApiResponseTest.TEST_FAIL);
        }
    }
}

package com.sparta.doguin.domain.test;

import com.sparta.doguin.domain.common.exception.TestException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseTest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "응답 테스트 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final TestService testService;

    @Operation(summary = "응답 테스트 1 - 데이터")
    @GetMapping("/data")
    public ResponseEntity<ApiResponse<String>> test1() {
        ApiResponse<String> apiResponse = testService.test1();
        return ApiResponse.of(apiResponse);
    }

    @Operation(summary = "응답 테스트 2 - 에러")
    @GetMapping("/error")
    public void test2() {
        try {
            throw new Exception("");
        } catch (Exception e) {
            throw new TestException(ApiResponseTest.TEST_FAIL);
        }
    }
}

package com.sparta.doguin.domain.test;

import com.sparta.doguin.domain.common.exception.TestException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseTest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "응답 테스트 API")
@RestController
@Slf4j(topic = "test")
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final TestService testService;
    private final RedisTemplate<String,Object> redisTemplate;
    private final MongoTemplate mongoTemplate;

    @Operation(summary = "응답 테스트 1 - 데이터")
    @GetMapping("/data")
    public ResponseEntity<ApiResponse<String>> test1() {
        log.info("저 들어왔어요 !");
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

    @GetMapping("/ip")
    public String test3(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress != null && !ipAddress.isEmpty()) {
            ipAddress = ipAddress.split(",")[0].trim();  // 첫 번째 IP만 가져옴
        } else {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    @GetMapping("/redis")
    public String test4() {
        redisTemplate.opsForValue().set("hello","world");
        return (String) redisTemplate.opsForValue().get("hello");
    }

    @GetMapping("/mongo")
    public String test5() {
        // "hello" 키와 "world" 값을 가진 객체 저장
        KeyValue keyValue = new KeyValue("hello", "world");
        mongoTemplate.save(keyValue, "keyValueCollection");

        // "hello" 키를 기준으로 값 조회
        KeyValue retrievedKeyValue = mongoTemplate.findById("hello", KeyValue.class, "keyValueCollection");

        return retrievedKeyValue != null ? retrievedKeyValue.getValue() : "데이터 없음";
    }

}

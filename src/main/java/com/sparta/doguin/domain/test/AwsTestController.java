package com.sparta.doguin.domain.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// TEST
@RestController
public class AwsTestController {
    @GetMapping("/health")
    public String albTest(){
        return "OK";
    }
}

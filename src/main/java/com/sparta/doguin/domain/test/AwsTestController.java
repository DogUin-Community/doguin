package com.sparta.doguin.domain.test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// TEST
@RestController
public class AwsTestController {
    @GetMapping("/health")
    public ResponseEntity<String> albTest() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}

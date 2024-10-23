package com.sparta.doguin.domain.common.exception;

import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> baseException(BaseException e) {
        ApiResponseEnum apiResponseEnum = e.getApiResponseEnum();
        ApiResponse<Void> apiResponse = new ApiResponse<>(apiResponseEnum);
        return ResponseEntity.status(apiResponse.getCode()).body(apiResponse);
    }

    // DTO exception custom
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> dtoException(MethodArgumentNotValidException e) {
        FieldError fe = e.getBindingResult().getFieldError();
        ApiResponse<Void> apiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), fe.getField() + " " + fe.getDefaultMessage(),null);
        return ApiResponse.of(apiResponse);
    }
}

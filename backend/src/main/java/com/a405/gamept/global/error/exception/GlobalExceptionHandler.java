package com.a405.gamept.global.error.exception;

import com.a405.gamept.global.error.dto.ErrorResponseDto;
import com.a405.gamept.global.error.exception.custom.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> businessExceptionHandle(BusinessException e) {
        log.warn("businessException : {}", e);
        return ResponseEntity.internalServerError()
                .body(ErrorResponseDto.of(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> allUncaughtHandle(Exception e) {
        log.error("allUncaughtHandle : {}", e);
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDto> methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {
        log.error("methodArgumentNotValidException : {}", e);
        return ResponseEntity.badRequest()
                .body(ErrorResponseDto.of(HttpStatus.BAD_REQUEST.value(), e.getFieldErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<ErrorResponseDto> methodArgumentNotValidExceptionHandle(BadRequestException e) {
        return ResponseEntity.badRequest()
                .body(ErrorResponseDto.of(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(InternalServerException.class)
    protected ResponseEntity<ErrorResponseDto> methodArgumentNotValidExceptionHandle(InternalServerException e) {
        return ResponseEntity.internalServerError()
                .body(ErrorResponseDto.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류"));
    }
}
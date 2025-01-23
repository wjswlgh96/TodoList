package com.example.todolist.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 전역 예외 처리 신세계다
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            InvalidPasswordException.class, NotFoundException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<String> handleInvalidPasswordException(RuntimeException e) {
        if (e instanceof InvalidPasswordException || e instanceof IllegalArgumentException) {
            log.error("잘못된 데이터 오류: {}", e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } else if (e instanceof NotFoundException) {
            log.error("데이터가 존재하지 않음: {}", e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

package com.example.todolist.exception;

import com.example.todolist.exception.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler {

    // 비밀번호 불일치 및 잘못된 요청
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidPasswordException(InvalidPasswordException e) {
        log.error("비밀번호 오류: {}", e.getMessage(), e);
        ErrorResponseDto response = new ErrorResponseDto(
                LocalDateTime.now(),                            // 발생 시간
                HttpStatus.BAD_REQUEST.value(),                 // HTTP 상태 코드
                HttpStatus.BAD_REQUEST.getReasonPhrase(),       // 상태 코드 설명
                e.getMessage()                                  // 사용자에게 전달될 메세지
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequestException(BadRequestException e) {
        log.error("잘못된 요청: {}", e.getMessage(), e);
        ErrorResponseDto response = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 중복된 데이터 예외 (email 을 UNIQUE로 만들었음)
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("중복된 데이터: {}", e.getMessage(), e);
        ErrorResponseDto response = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                "이미 존재하는 데이터 입니다. 다시 시도해 주세요: " + e.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // 데이터 조회 실패
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException e) {
        log.error("데이터를 찾을 수 없음: {}", e.getMessage(), e);
        ErrorResponseDto response = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // 데이터 검증 @Valid 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.error("데이터 검증 예외: {}", errorMessage, e);
        ErrorResponseDto response = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errorMessage
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 예상치 못한 예외
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleGeneralException(RuntimeException e) {
        log.error("런타임 예외 발생: {}", e.getMessage(), e);
        ErrorResponseDto response = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요"
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

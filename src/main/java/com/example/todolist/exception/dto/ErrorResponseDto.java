package com.example.todolist.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor      // 파라미터가 없는 디폴트 생성자
@AllArgsConstructor     // 모든 필드값을 파라미터로 받는 생성자
public class ErrorResponseDto {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
}

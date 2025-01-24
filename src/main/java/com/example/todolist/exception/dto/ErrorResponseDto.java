package com.example.todolist.exception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor      // 파라미터가 없는 디폴트 생성자
@AllArgsConstructor     // 모든 필드값을 파라미터로 받는 생성자
@Schema(description = "에러 발생 Dto")
public class ErrorResponseDto {

    @Schema(description = "에러 발생 시각", example = "2023-10-10T12:34:56.789")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP 상태 코드", example = "400")
    private int status;

    @Schema(description = "HTTP 상태 설명", example = "BAD_REQUEST")
    private String error;

    @Schema(description = "에러 메시지", example = "페이지는 1 이상, 사이즈는 1 이상 100 이하여야 합니다.")
    private String message;
}

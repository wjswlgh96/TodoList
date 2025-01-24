package com.example.todolist.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardPasswordResponseDto {
    private Long id;
    private Long authorId;
    private String password;
    private String authorName;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

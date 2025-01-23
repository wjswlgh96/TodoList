package com.example.todolist.board.dto;

import com.example.todolist.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BoardResponseDto {
    private Long id;
    private Long authorId;
    private String authorName;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BoardResponseDto(BoardPasswordResponseDto dto) {
        this.id = dto.getId();
        this.authorId = dto.getAuthorId();
        this.authorName = dto.getAuthorName();
        this.title = dto.getTitle();
        this.contents = dto.getContents();
        this.createdAt = dto.getCreatedAt();
        this.updatedAt = dto.getUpdatedAt();
    }
}

package com.example.todolist.author.dto;

import com.example.todolist.author.entity.Author;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AuthorResponseDto {

    @Schema(example = "1")
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AuthorResponseDto(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.email = author.getEmail();
        this.createdAt = author.getCreatedAt();
        this.updatedAt = author.getUpdatedAt();
    }
}

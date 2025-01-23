package com.example.todolist.board.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Board {

    private Long id;
    private String author;
    private String password;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Board(Long id, String author, String title, String contents, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.contents = contents;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Board(String author, String password, String title, String contents, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.author = author;
        this.password = password;
        this.title = title;
        this.contents = contents;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateBoard(String author, String contents) {
        this.author = author;
        this.contents = contents;
        this.updatedAt = LocalDateTime.now();
    }
}

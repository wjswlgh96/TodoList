package com.example.todolist.board.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Board {

    private Long id;
    private Long authorId;
    private String password;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Board(Long authorId, String password, String title, String contents) {
        this.authorId = authorId;
        this.password = password;
        this.title = title;
        this.contents = contents;
    }

    public void updateBoard(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}

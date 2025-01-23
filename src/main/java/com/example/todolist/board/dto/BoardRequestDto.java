package com.example.todolist.board.dto;

import lombok.Getter;

@Getter
public class BoardRequestDto {
    private String author;
    private String password;
    private String title;
    private String contents;
}

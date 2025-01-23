package com.example.todolist.board.dto;

import lombok.Getter;

@Getter
public class BoardRequestDto {
    private Long author_id;
    private String password;
    private String title;
    private String contents;
}

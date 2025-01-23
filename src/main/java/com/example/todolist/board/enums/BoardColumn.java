package com.example.todolist.board.enums;

import lombok.Getter;

@Getter
public enum BoardColumn {
    ID("id"),
    AUTHOR_ID("author_id"),
    PASSWORD("password"),
    TITLE("title"),
    CONTENTS("contents"),
    CREATED_AT("created_at"),
    UPDATED_AT("updated_at");

    private final String columnName;

    BoardColumn(String columnName) {
        this.columnName = columnName;
    }

}

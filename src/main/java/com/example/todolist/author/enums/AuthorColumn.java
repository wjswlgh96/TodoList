package com.example.todolist.author.enums;

import lombok.Getter;

@Getter
public enum AuthorColumn {
    ID("id"),
    NAME("name"),
    EMAIL("email"),
    CREATED_AT("created_at"),
    UPDATED_AT("updated_at");

    private final String columnName;

    AuthorColumn(String columnName) {
        this.columnName = columnName;
    }

}

package com.example.todolist.author.enums;

import lombok.Getter;

@Getter
public enum AuthorSQL {
    QUERY_FIND_ALL("SELECT * FROM author"),
    QUERY_FIND_BY_ID("SELECT * FROM author WHERE id = ?"),
    QUERY_UPDATE("UPDATE author SET name = ?, email = ? WHERE id = ?"),
    QUERY_DELETE("DELETE FROM author WHERE id = ?");

    private final String sql;

    AuthorSQL(String sql) {
        this.sql = sql;
    }

}

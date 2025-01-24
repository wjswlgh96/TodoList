package com.example.todolist.board.enums;

import lombok.Getter;

@Getter
public enum BoardSQL {

    QUERY_FIND_BY_ID_AUTHOR("SELECT * FROM author WHERE id = ?"),
    QUERY_FIND_ALL(getDefaultQuery() + " WHERE 1=1"),
    QUERY_FIND_BY_ID(getDefaultQuery() + " WHERE b.id = ?"),
    QUERY_WHERE_AUTHOR_ID(" AND b.author_id = ?"),
    QUERY_WHERE_CREATED_AT(" AND DATE(created_at) = ?"),
    QUERY_ORDER_BY_CREATE_AT_AND_USE_PAGINATION(" ORDER BY b.created_at DESC LIMIT ? OFFSET ?"),
    QUERY_SELECT_ALL_COUNT("SELECT COUNT(*) FROM board b JOIN author a ON b.author_id = a.id WHERE 1=1"),
    QUERY_UPDATE("UPDATE board SET title = ?, contents = ? WHERE id = ?"),
    QUERY_DELETE("DELETE FROM board WHERE id = ?");

    private final String sql;

    BoardSQL(String sql) {
        this.sql = sql;
    }

    private static String getDefaultQuery() {
        return "SELECT b.*, a.name AS author_name " +
                "FROM board AS b " +
                "JOIN author AS a ON b.author_id = a.id";
    }
}

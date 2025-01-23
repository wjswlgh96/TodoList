package com.example.todolist.board.entity;

import lombok.Getter;

@Getter
public class Paging {

    private int page;
    private int size;

    public Paging(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int getOffset() {
        return (page - 1) * size;
    }
}

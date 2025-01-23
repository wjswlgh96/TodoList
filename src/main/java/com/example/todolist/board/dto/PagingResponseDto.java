package com.example.todolist.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PagingResponseDto<T> {
    private List<T> items;
    private int currentPage;
    private int pageSize;
    private long totalItems;
    private int totalPages;

}

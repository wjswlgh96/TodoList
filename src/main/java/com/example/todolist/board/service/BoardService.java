package com.example.todolist.board.service;

import com.example.todolist.board.dto.request.BoardRequestDto;
import com.example.todolist.board.dto.response.BoardResponseDto;
import com.example.todolist.board.dto.response.PagingResponseDto;
import com.example.todolist.board.entity.Paging;

import java.util.List;

public interface BoardService {
    BoardResponseDto saveBoard(BoardRequestDto requestDto);

    List<BoardResponseDto> findAllBoards(String createdAt, Long authorId);

    PagingResponseDto<BoardResponseDto> findAllBoards(String createdAt, Long authorId, Paging paging);

    BoardResponseDto findBoardById(Long id);

    BoardResponseDto updateBoard(Long id, String password, String title, String contents);

    void deleteMemo(Long id, String password);
}

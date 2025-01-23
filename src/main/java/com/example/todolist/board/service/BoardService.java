package com.example.todolist.board.service;

import com.example.todolist.board.dto.BoardRequestDto;
import com.example.todolist.board.dto.BoardResponseDto;

import java.util.List;

public interface BoardService {
    BoardResponseDto saveBoard(BoardRequestDto requestDto);

    List<BoardResponseDto> findAllBoards(String createdAt, String author);

    BoardResponseDto findBoardById(Long id);

    BoardResponseDto updateBoard(Long id, String password, String author, String contents);

}

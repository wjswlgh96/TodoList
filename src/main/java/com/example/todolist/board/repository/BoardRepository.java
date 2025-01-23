package com.example.todolist.board.repository;

import com.example.todolist.board.dto.BoardResponseDto;
import com.example.todolist.board.entity.Board;

import java.util.List;

public interface BoardRepository {
    BoardResponseDto saveBoard(Board board);

    List<BoardResponseDto> findAllBoards(String createdAt, Long authorId);

    Board findBoardByIdOrElseThrow(Long id);

    int updateBoard(Long id, String title, String contents);

    int deleteBoard(Long id);
}

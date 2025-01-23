package com.example.todolist.board.service;

import com.example.todolist.board.dto.BoardRequestDto;
import com.example.todolist.board.dto.BoardResponseDto;
import com.example.todolist.board.entity.Board;
import com.example.todolist.board.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public BoardResponseDto saveBoard(BoardRequestDto requestDto) {
        Board board = new Board(
                requestDto.getAuthor(),
                requestDto.getPassword(),
                requestDto.getTitle(),
                requestDto.getContents(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        return boardRepository.saveBoard(board);
    }

    @Override
    public List<BoardResponseDto> findAllBoards(String createdAt, String author) {
        return boardRepository.findAllBoards(createdAt, author);
    }

    @Override
    public BoardResponseDto findBoardById(Long id) {
        return boardRepository.findMemoByIdOrElseThrow(id);
    }
}

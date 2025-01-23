package com.example.todolist.board.controller;

import com.example.todolist.board.dto.BoardRequestDto;
import com.example.todolist.board.dto.BoardResponseDto;
import com.example.todolist.board.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    private BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto requestDto) {
        return new ResponseEntity<>(boardService.saveBoard(requestDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> findAllBoards(
            @RequestParam(required = false) String created_at,
            @RequestParam(required = false) String author
    ) {
        return new ResponseEntity<>(boardService.findAllBoards(created_at, author), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> findBoardById(@PathVariable Long id) {
        return new ResponseEntity<>(boardService.findBoardById(id), HttpStatus.OK);
    }
}

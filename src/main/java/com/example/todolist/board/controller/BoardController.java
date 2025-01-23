package com.example.todolist.board.controller;

import com.example.todolist.board.dto.BoardRequestDto;
import com.example.todolist.board.dto.BoardResponseDto;
import com.example.todolist.board.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(required = false) Long author_id
    ) {
        return new ResponseEntity<>(boardService.findAllBoards(created_at, author_id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> findBoardById(@PathVariable Long id) {
        return new ResponseEntity<>(boardService.findBoardById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        return new ResponseEntity<>(boardService.updateBoard(id, requestDto.getPassword(), requestDto.getTitle(), requestDto.getContents()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        boardService.deleteMemo(id, requestDto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

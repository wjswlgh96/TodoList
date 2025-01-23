package com.example.todolist.board.controller;

import com.example.todolist.board.dto.BoardRequestDto;
import com.example.todolist.board.dto.BoardResponseDto;
import com.example.todolist.board.dto.PagingResponseDto;
import com.example.todolist.board.service.BoardService;
import com.example.todolist.board.entity.Paging;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<PagingResponseDto<BoardResponseDto>> findAllBoards(
            @RequestParam(value = "created_at", required = false) String createdAt,
            @RequestParam(value = "author_id", required = false) Long authorId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        if (page != null && size != null) {
            if (page < 1 || size < 1 || size > 100) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page number and size must be greater than 0 and size must be less than or equal to 100");
            }
            Paging paging = new Paging(page, size);
            PagingResponseDto<BoardResponseDto> response = boardService.findAllBoards(createdAt, authorId, paging);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            List<BoardResponseDto> boards = boardService.findAllBoards(createdAt, authorId);
            PagingResponseDto<BoardResponseDto> response = new PagingResponseDto<>(
                    boards,
                    1,
                    boards.size(),
                    boards.size(),
                    1
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
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

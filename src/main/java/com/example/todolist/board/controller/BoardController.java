package com.example.todolist.board.controller;

import com.example.todolist.board.dto.BoardRequestDto;
import com.example.todolist.board.dto.BoardResponseDto;
import com.example.todolist.board.dto.PagingResponseDto;
import com.example.todolist.board.service.BoardService;
import com.example.todolist.board.entity.Paging;
import com.example.todolist.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // Dto를 interface화 시킬 수 있나 고민했다가 포기하고 ?로 타입에 자유를 줬음
    @GetMapping
    public ResponseEntity<?> findAllBoards(
            @RequestParam(value = "created_at", required = false) String createdAt,
            @RequestParam(value = "author_id", required = false) Long authorId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        // PagingResponseDto = Pagination을 위한 Dto
        if (page != null && size != null) {
            if (page < 1 || size < 1 || size > 100) {
                throw new BadRequestException("페이지, 사이즈는 0보다 커야하고, 사이즈는 100보단 작아야 합니다.");
            }
            Paging paging = new Paging(page, size);
            PagingResponseDto<BoardResponseDto> response = boardService.findAllBoards(createdAt, authorId, paging);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(boardService.findAllBoards(createdAt, authorId), HttpStatus.OK);
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

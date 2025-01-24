package com.example.todolist.board.controller;

import com.example.todolist.board.dto.request.BoardRequestDto;
import com.example.todolist.board.dto.response.BoardResponseDto;
import com.example.todolist.board.dto.request.BoardUpdateRequestDto;
import com.example.todolist.board.dto.response.PagingResponseDto;
import com.example.todolist.board.service.BoardService;
import com.example.todolist.board.entity.Paging;
import com.example.todolist.exception.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
@Tag(name = "Board API", description = "게시글 API")
public class BoardController {

    private final BoardService boardService;

    private BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    @Operation(summary = "게시글 등록", description = "새로운 게시글을 등록합니다.")
    public ResponseEntity<BoardResponseDto> createBoard(@Valid @RequestBody BoardRequestDto requestDto) {
        return new ResponseEntity<>(boardService.saveBoard(requestDto), HttpStatus.OK);
    }

    // Dto를 interface화 시킬 수 있나 고민했다가 포기하고 ?로 타입에 자유를 줬음
    @GetMapping
    @Operation(summary = "게시글 조회", description = "모든 게시글 목록을 조회합니다.")
    public ResponseEntity<?> findAllBoards(
            @RequestParam(value = "created_at", required = false)
            @Schema(description = "게시글 생성 날짜 (형식: YYYY-MM-DD)") String createdAt,

            @RequestParam(value = "author_id", required = false)
            @Schema(description = "작성자 ID (숫자 입력, 예: 1)", type = "number") Long authorId,

            @RequestParam(value = "page", required = false)
            @Schema(description = "페이지 번호 (size와 필수로 함께 사용)", type = "number") Integer page,

            @RequestParam(value = "size", required = false)
            @Schema(description = "페이지 사이즈 (page와 필수로 함께 사용)", type = "number") Integer size
    ) {
        // PagingResponseDto = Pagination을 위한 Dto
        if (page != null && size != null) {
            if (page < 1 || size < 1 || size > 100) {
                throw new BadRequestException("페이지는 1 이상, 사이즈는 1 이상 100 이하여야 합니다.");
            }
            Paging paging = new Paging(page, size);
            PagingResponseDto<BoardResponseDto> response = boardService.findAllBoards(createdAt, authorId, paging);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(boardService.findAllBoards(createdAt, authorId), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "게시글 상세 조회", description = "주어진 ID로 특정 게시글의 정보를 조회합니다.")
    public ResponseEntity<BoardResponseDto> findBoardById(@PathVariable Long id) {
        return new ResponseEntity<>(boardService.findBoardById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "게시글 수정", description = "주어진 ID에 해당하는 게시글의 정보를 수정합니다.")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long id, @Valid @RequestBody BoardUpdateRequestDto requestDto) {
        return new ResponseEntity<>(boardService.updateBoard(id, requestDto.getPassword(), requestDto.getTitle(), requestDto.getContents()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제", description = "주어진 ID에 해당하는 게시글을 삭제합니다.")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id, @Valid @RequestBody BoardRequestDto requestDto) {
        boardService.deleteMemo(id, requestDto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

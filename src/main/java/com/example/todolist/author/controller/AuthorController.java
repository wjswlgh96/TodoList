package com.example.todolist.author.controller;

import com.example.todolist.author.dto.AuthorRequestDto;
import com.example.todolist.author.dto.AuthorResponseDto;
import com.example.todolist.author.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
@Tag(name = "Author API", description = "작성자 API")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @Operation(summary = "작성자 등록", description = "새로운 작성자를 등록합니다.")
    public ResponseEntity<AuthorResponseDto> createAuthor(@Valid @RequestBody AuthorRequestDto requestDto) {

        return new ResponseEntity<>(authorService.saveAuthor(requestDto), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "작성자 목록 조회", description = "모든 작성자 목록을 조회합니다.")
    public ResponseEntity<List<AuthorResponseDto>> findAllAuthors() {
        return new ResponseEntity<>(authorService.findAllAuthors(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 작성자 조회", description = "주어진 ID로 특정 작성자의 정보를 조회합니다.")
    public ResponseEntity<AuthorResponseDto> findAuthorById(@PathVariable Long id) {
        return new ResponseEntity<>(authorService.findAuthorById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "작성자 정보 수정", description = "주어진 ID에 해당하는 작성자의 정보를 수정합니다.")
    public ResponseEntity<AuthorResponseDto> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorRequestDto requestDto) {
        return new ResponseEntity<>(authorService.updateAuthor(id, requestDto.getName(), requestDto.getEmail()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "작성자 삭제", description = "주어진 ID에 해당하는 작성자를 삭제합니다.")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

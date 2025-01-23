package com.example.todolist.author.service;

import com.example.todolist.author.dto.AuthorRequestDto;
import com.example.todolist.author.dto.AuthorResponseDto;

import java.util.List;

public interface AuthorService {
    AuthorResponseDto saveAuthor(AuthorRequestDto requestDto);

    List<AuthorResponseDto> findAllAuthors();

    AuthorResponseDto findAuthorById(Long id);

    AuthorResponseDto updateAuthor(Long id, String name, String email);

    void deleteAuthor(Long id);
}

package com.example.todolist.author.repository;

import com.example.todolist.author.dto.AuthorResponseDto;
import com.example.todolist.author.entity.Author;

import java.util.List;

public interface AuthorRepository {
    AuthorResponseDto saveAuthor(Author author);

    List<AuthorResponseDto> findAllAuthors();

    Author findAuthorByIdOrElseThrow(Long id);

    int updateAuthor(Long id, String name, String email);

    int deleteAuthor(Long id);
}

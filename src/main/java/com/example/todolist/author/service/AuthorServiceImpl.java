package com.example.todolist.author.service;

import com.example.todolist.author.dto.AuthorRequestDto;
import com.example.todolist.author.dto.AuthorResponseDto;
import com.example.todolist.author.entity.Author;
import com.example.todolist.author.repository.AuthorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorResponseDto saveAuthor(AuthorRequestDto requestDto) {
        Author author = new Author(
                requestDto.getName(),
                requestDto.getEmail()
        );

        return authorRepository.saveAuthor(author);
    }

    @Override
    public List<AuthorResponseDto> findAllAuthors() {
        return authorRepository.findAllAuthors();
    }

    @Override
    public AuthorResponseDto findAuthorById(Long id) {
        return new AuthorResponseDto(authorRepository.findAuthorByIdOrElseThrow(id));
    }

    @Override
    public AuthorResponseDto updateAuthor(Long id, String name, String email) {
        if (name == null || email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The name and email are required values.");
        }

        int updatedRow = authorRepository.updateAuthor(id, name, email);
        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data has been modified.");
        }

        Author author = authorRepository.findAuthorByIdOrElseThrow(id);
        return new AuthorResponseDto(author);
    }

    @Override
    public void deleteAuthor(Long id) {
        int deletedRow = authorRepository.deleteAuthor(id);
        if (deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data has been modified.");
        }
    }
}

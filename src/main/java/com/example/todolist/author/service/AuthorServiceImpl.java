package com.example.todolist.author.service;

import com.example.todolist.author.dto.AuthorRequestDto;
import com.example.todolist.author.dto.AuthorResponseDto;
import com.example.todolist.author.entity.Author;
import com.example.todolist.author.repository.AuthorRepository;
import com.example.todolist.exception.BadRequestException;
import com.example.todolist.exception.NotFoundException;
import org.springframework.stereotype.Service;

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
            throw new BadRequestException("이름이나 이메일 값이 빠졌습니다. 두 값은 필수입니다.");
        }

        int updatedRow = authorRepository.updateAuthor(id, name, email);
        if (updatedRow == 0) {
            throw new NotFoundException("수정할 작성자가 존재하지 않습니다. 아이디를 확인해주세요.");
        }

        Author author = authorRepository.findAuthorByIdOrElseThrow(id);
        return new AuthorResponseDto(author);
    }

    @Override
    public void deleteAuthor(Long id) {
        int deletedRow = authorRepository.deleteAuthor(id);
        if (deletedRow == 0) {
            throw new NotFoundException("삭제할 작성자가 존재하지 않습니다. 아이디를 확인해주세요.");
        }
    }
}

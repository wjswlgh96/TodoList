package com.example.todolist.board.service;

import com.example.todolist.board.dto.BoardRequestDto;
import com.example.todolist.board.dto.BoardResponseDto;
import com.example.todolist.board.entity.Board;
import com.example.todolist.board.repository.BoardRepository;
import com.sun.net.httpserver.HttpsServer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public BoardResponseDto saveBoard(BoardRequestDto requestDto) {
        Board board = new Board(
                requestDto.getAuthor(),
                requestDto.getPassword(),
                requestDto.getTitle(),
                requestDto.getContents(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        return boardRepository.saveBoard(board);
    }

    @Override
    public List<BoardResponseDto> findAllBoards(String createdAt, String author) {
        return boardRepository.findAllBoards(createdAt, author);
    }

    @Override
    public BoardResponseDto findBoardById(Long id) {
        return new BoardResponseDto(boardRepository.findBoardByIdOrElseThrow(id));
    }

    @Transactional
    @Override
    public BoardResponseDto updateBoard(Long id, String password, String author, String contents) {
        if (author == null || contents == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The author and contents are required values.");
        }

        Board board = boardRepository.findBoardByIdOrElseThrow(id);
        if (!validatePassword(board.getPassword(), password)) {
            System.out.println("board.getPassword() = " + board.getPassword());
            System.out.println("password = " + password);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Password");
        }

        int updatedRow = boardRepository.updateBoard(id, author, contents);
        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data has been modified.");
        }
        board.updateBoard(author, contents);
        return new BoardResponseDto(board);
    }

    private boolean validatePassword(String dbPassword, String password) {
        return dbPassword.equals(password);
    }
}

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
                requestDto.getAuthor_id(),
                requestDto.getPassword(),
                requestDto.getTitle(),
                requestDto.getContents()
        );
        return boardRepository.saveBoard(board);
    }

    @Override
    public List<BoardResponseDto> findAllBoards(String createdAt, Long authorId) {
        return boardRepository.findAllBoards(createdAt, authorId);
    }

    @Override
    public BoardResponseDto findBoardById(Long id) {
        return new BoardResponseDto(boardRepository.findBoardByIdOrElseThrow(id));
    }

    @Transactional
    @Override
    public BoardResponseDto updateBoard(Long id, String password, String title, String contents) {
        if (title == null || contents == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The title and contents are required values.");
        }

        Board board = boardRepository.findBoardByIdOrElseThrow(id);
        if (!validatePassword(board.getPassword(), password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Password");
        }

        int updatedRow = boardRepository.updateBoard(id, title, contents);
        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data has been modified.");
        }

        board = boardRepository.findBoardByIdOrElseThrow(id);
        return new BoardResponseDto(board);
    }

    @Override
    public void deleteMemo(Long id, String password) {
        Board board = boardRepository.findBoardByIdOrElseThrow(id);
        if (!validatePassword(board.getPassword(), password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Password");
        }

        int deletedRow = boardRepository.deleteBoard(id);
        if (deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }
    }

    private boolean validatePassword(String dbPassword, String password) {
        return dbPassword.equals(password);
    }
}

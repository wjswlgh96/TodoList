package com.example.todolist.board.service;

import com.example.todolist.board.dto.BoardPasswordResponseDto;
import com.example.todolist.board.dto.BoardRequestDto;
import com.example.todolist.board.dto.BoardResponseDto;
import com.example.todolist.board.dto.PagingResponseDto;
import com.example.todolist.board.entity.Board;
import com.example.todolist.board.repository.BoardRepository;
import com.example.todolist.board.entity.Paging;
import com.example.todolist.exception.NotFoundException;
import com.example.todolist.exception.IllegalArgumentException;
import com.example.todolist.exception.InvalidPasswordException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PagingResponseDto<BoardResponseDto> findAllBoards(String createdAt, Long authorId, Paging paging) {
        return boardRepository.findAllBoards(createdAt, authorId, paging);
    }

    @Override
    public BoardResponseDto findBoardById(Long id) {
        return new BoardResponseDto(boardRepository.findBoardByIdOrElseThrow(id));
    }

    @Transactional
    @Override
    public BoardResponseDto updateBoard(Long id, String password, String title, String contents) {
        if (title == null || contents == null) {
            throw new IllegalArgumentException("제목이나 내용 값이 빠졌습니다. 두 값은 필수입니다.");
        }

        BoardPasswordResponseDto board = boardRepository.findBoardByIdOrElseThrow(id);
        if (!validatePassword(board.getPassword(), password)) {
            throw new InvalidPasswordException();
        }

        int updatedRow = boardRepository.updateBoard(id, title, contents);
        if (updatedRow == 0) {
            throw new NotFoundException("수정할 게시글이 존재하지 않습니다. 아이디를 확인해주세요.");
        }

        board = boardRepository.findBoardByIdOrElseThrow(id);
        return new BoardResponseDto(board);
    }

    @Override
    public void deleteMemo(Long id, String password) {
        BoardPasswordResponseDto board = boardRepository.findBoardByIdOrElseThrow(id);
        if (!validatePassword(board.getPassword(), password)) {
            throw new InvalidPasswordException();
        }

        int deletedRow = boardRepository.deleteBoard(id);
        if (deletedRow == 0) {
            throw new NotFoundException("삭제할 게시글이 존재하지 않습니다. 아이디를 확인해주세요.");
        }
    }

    private boolean validatePassword(String dbPassword, String password) {
        return dbPassword.equals(password);
    }
}

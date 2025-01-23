package com.example.todolist.board.repository;

import com.example.todolist.board.dto.BoardRequestDto;
import com.example.todolist.board.dto.BoardResponseDto;
import com.example.todolist.board.entity.Board;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTemplateBoardRepository implements BoardRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateBoardRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public BoardResponseDto saveBoard(Board board) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("board").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("password", board.getPassword());
        parameters.put("author", board.getAuthor());
        parameters.put("title", board.getTitle());
        parameters.put("contents", board.getContents());
        parameters.put("created_at", board.getCreatedAt());
        parameters.put("updated_at", board.getUpdatedAt());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return new BoardResponseDto(
                key.longValue(),
                board.getAuthor(),
                board.getTitle(),
                board.getContents(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }

    @Override
    public List<BoardResponseDto> findAllBoards(String createdAt, String author) {
        String sql = "select * from board where 1=1";

        List<String> params = new ArrayList<>();

        if (author != null) {
            sql += " and author = ?";
            params.add(author);
        }

        if (createdAt != null) {
            sql += " and DATE(created_at) = ? order by created_at DESC";
            params.add(createdAt);
        }

        return jdbcTemplate.query(sql, params.toArray(), boardResponseRowRapper());
    }

    @Override
    public Board findBoardByIdOrElseThrow(Long id) {
        List<Board> result = jdbcTemplate.query("select * from board where id = ?", boardRowRapper(), id);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id));
    }

    @Override
    public int updateBoard(Long id, String author, String contents) {
        return jdbcTemplate.update("update board set author = ?, contents = ?, updated_at = ? where id = ?", author, contents, LocalDateTime.now(), id);
    }

    private RowMapper<BoardResponseDto> boardResponseRowRapper() {
        return new RowMapper<BoardResponseDto>() {
            @Override
            public BoardResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new BoardResponseDto(
                        rs.getLong("id"),
                        rs.getString("author"),
                        rs.getString("title"),
                        rs.getString("contents"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                );
            }
        };
    }

    private RowMapper<Board> boardRowRapper() {
        return new RowMapper<Board>() {
            @Override
            public Board mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Board(
                        rs.getLong("id"),
                        rs.getString("author"),
                        rs.getString("password"),
                        rs.getString("title"),
                        rs.getString("contents"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                );
            }
        };
    }
}

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
        jdbcInsert.withTableName("board")
                .usingColumns("password", "author_id", "title", "contents")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("password", board.getPassword());
        parameters.put("author_id", board.getAuthorId());
        parameters.put("title", board.getTitle());
        parameters.put("contents", board.getContents());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        Board insertedBoard = findBoardByIdOrElseThrow(key.longValue());

        return new BoardResponseDto(
                insertedBoard.getId(),
                insertedBoard.getAuthorId(),
                insertedBoard.getTitle(),
                insertedBoard.getContents(),
                insertedBoard.getCreatedAt(),
                insertedBoard.getUpdatedAt()
        );
    }

    @Override
    public List<BoardResponseDto> findAllBoards(String createdAt, Long authorId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM board WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (authorId != null) {
            sql.append(" AND author_id = ?");
            params.add(authorId);
        }

        if (createdAt != null) {
            sql.append(" AND DATE(created_at) = ?");
            params.add(createdAt);
        }
        return jdbcTemplate.query(sql.toString(), params.toArray(), boardResponseRowRapper());
    }

    @Override
    public Board findBoardByIdOrElseThrow(Long id) {
        List<Board> result = jdbcTemplate.query("select * from board where id = ?", boardRowRapper(), id);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id));
    }

    @Override
    public int updateBoard(Long id, String title, String contents) {
        return jdbcTemplate.update("update board set title = ?, contents = ? where id = ?", title, contents, id);
    }

    @Override
    public int deleteBoard(Long id) {
        return jdbcTemplate.update("delete from board where id = ?", id);
    }

    private RowMapper<BoardResponseDto> boardResponseRowRapper() {
        return new RowMapper<BoardResponseDto>() {
            @Override
            public BoardResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new BoardResponseDto(
                        rs.getLong("id"),
                        rs.getLong("author_id"),
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
                        rs.getLong("author_id"),
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

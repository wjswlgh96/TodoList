package com.example.todolist.board.repository;

import com.example.todolist.board.dto.BoardPasswordResponseDto;
import com.example.todolist.board.dto.BoardResponseDto;
import com.example.todolist.board.dto.PagingResponseDto;
import com.example.todolist.board.entity.Board;
import com.example.todolist.board.enums.BoardColumn;
import com.example.todolist.board.entity.Paging;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Transactional
    @Override
    public BoardResponseDto saveBoard(Board board) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("board")
                .usingColumns(
                        BoardColumn.PASSWORD.getColumnName(),
                        BoardColumn.AUTHOR_ID.getColumnName(),
                        BoardColumn.TITLE.getColumnName(),
                        BoardColumn.CONTENTS.getColumnName()
                )
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(BoardColumn.PASSWORD.getColumnName(), board.getPassword());
        parameters.put(BoardColumn.AUTHOR_ID.getColumnName(), board.getAuthorId());
        parameters.put(BoardColumn.TITLE.getColumnName(), board.getTitle());
        parameters.put(BoardColumn.CONTENTS.getColumnName(), board.getContents());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        List<BoardResponseDto> result = jdbcTemplate.query("SELECT b.*, a.name AS author_name FROM board AS b JOIN author AS a ON b.author_id = a.id WHERE b.id = ?", new Object[]{key.longValue()}, boardResponseRowMapper());
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + key.longValue()));
    }

    @Override
    public List<BoardResponseDto> findAllBoards(String createdAt, Long authorId) {
        StringBuilder sql = new StringBuilder("SELECT b.*, a.name AS author_name FROM board AS b JOIN author AS a ON b.author_id = a.id WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (authorId != null) {
            sql.append(" AND author_id = ?");
            params.add(authorId);
        }

        if (createdAt != null) {
            sql.append(" AND DATE(created_at) = ?");
            params.add(createdAt);
        }

        return jdbcTemplate.query(sql.toString(), params.toArray(), boardResponseRowMapper());
    }

    @Transactional
    @Override
    public PagingResponseDto<BoardResponseDto> findAllBoards(String createdAt, Long authorId, Paging paging) {
        String countSql = "SELECT COUNT(*) FROM board b JOIN author a ON b.author_id = a.id WHERE 1=1";
        List<Object> countParams = new ArrayList<>();

        if (authorId != null) {
            countSql += " AND b.author_id = ?";
            countParams.add(authorId);
        }

        if (createdAt != null) {
            countSql += " AND DATE(b.created_at) = ?";
            countParams.add(createdAt);
        }

        Long totalItems = jdbcTemplate.queryForObject(countSql, countParams.toArray(), Long.class);

        // 데이터 조회
        String dataSql = "SELECT b.*, a.name AS author_name FROM board b JOIN author a ON b.author_id = a.id WHERE 1=1";
        List<Object> dataParams = new ArrayList<>();

        if (authorId != null) {
            dataSql += " AND b.author_id = ?";
            dataParams.add(authorId);
        }

        if (createdAt != null) {
            dataSql += " AND DATE(b.created_at) = ?";
            dataParams.add(createdAt);
        }

        dataSql += " ORDER BY b.created_at DESC LIMIT ? OFFSET ?";
        dataParams.add(paging.getSize());
        dataParams.add(paging.getOffset());

        List<BoardResponseDto> items = jdbcTemplate.query(dataSql, dataParams.toArray(), boardResponseRowMapper());
        int totalPages = (int) Math.ceil((double) totalItems / paging.getSize());

        return new PagingResponseDto<>(
                items,
                paging.getPage(),
                paging.getSize(),
                totalItems,
                totalPages
        );
    }

    @Override
    public BoardPasswordResponseDto findBoardByIdOrElseThrow(Long id) {
        List<BoardPasswordResponseDto> result = jdbcTemplate.query("SELECT b.*, a.name AS author_name FROM board AS b JOIN author AS a ON b.author_id = a.id WHERE b.id = ?", boardRowMapper(), id);
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

    private RowMapper<BoardResponseDto> boardResponseRowMapper() {
        return new RowMapper<BoardResponseDto>() {
            @Override
            public BoardResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new BoardResponseDto(
                        rs.getLong(BoardColumn.ID.getColumnName()),
                        rs.getLong(BoardColumn.AUTHOR_ID.getColumnName()),
                        rs.getString(BoardColumn.AUTHOR_NAME.getColumnName()),
                        rs.getString(BoardColumn.TITLE.getColumnName()),
                        rs.getString(BoardColumn.CONTENTS.getColumnName()),
                        rs.getTimestamp(BoardColumn.CREATED_AT.getColumnName()).toLocalDateTime(),
                        rs.getTimestamp(BoardColumn.UPDATED_AT.getColumnName()).toLocalDateTime()
                );
            }
        };
    }

    private RowMapper<BoardPasswordResponseDto> boardRowMapper() {
        return new RowMapper<BoardPasswordResponseDto>() {
            @Override
            public BoardPasswordResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new BoardPasswordResponseDto(
                        rs.getLong(BoardColumn.ID.getColumnName()),
                        rs.getLong(BoardColumn.AUTHOR_ID.getColumnName()),
                        rs.getString(BoardColumn.PASSWORD.getColumnName()),
                        rs.getString(BoardColumn.AUTHOR_NAME.getColumnName()),
                        rs.getString(BoardColumn.TITLE.getColumnName()),
                        rs.getString(BoardColumn.CONTENTS.getColumnName()),
                        rs.getTimestamp(BoardColumn.CREATED_AT.getColumnName()).toLocalDateTime(),
                        rs.getTimestamp(BoardColumn.UPDATED_AT.getColumnName()).toLocalDateTime()
                );
            }
        };
    }
}

package com.example.todolist.board.repository;

import com.example.todolist.board.dto.response.BoardPasswordResponseDto;
import com.example.todolist.board.dto.response.BoardResponseDto;
import com.example.todolist.board.dto.response.PagingResponseDto;
import com.example.todolist.board.entity.Board;
import com.example.todolist.board.enums.BoardColumn;
import com.example.todolist.board.entity.Paging;
import com.example.todolist.board.enums.BoardSQL;
import com.example.todolist.exception.NotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.todolist.board.enums.BoardSQL.*;

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
        long id = key.longValue();
        List<BoardResponseDto> result = jdbcTemplate.query(
                QUERY_FIND_BY_ID.getSql(),
                new Object[]{id}, boardResponseRowMapper()
        );
        
        // created_at과 updated_at을 자동화시켜서 id를 다시 받고 적용시켜줌 author_name 도 DB에 추가되어있지 않음이 이유
        return result.stream().findAny().orElseThrow(() -> new NotFoundException("해당 아이디의 게시글이 존재하지 않습니다 id = " + id));
    }

    @Override
    public List<BoardResponseDto> findAllBoards(String createdAt, Long authorId) {
        StringBuilder sql = new StringBuilder(QUERY_FIND_ALL.getSql());
        List<Object> params = new ArrayList<>();

        if (authorId != null) {
            sql.append(QUERY_WHERE_AUTHOR_ID.getSql());
            params.add(authorId);
        }

        if (createdAt != null) {
            sql.append(QUERY_WHERE_CREATED_AT.getSql());
            params.add(createdAt);
        }

        return jdbcTemplate.query(sql.toString(), params.toArray(), boardResponseRowMapper());
    }

    @Transactional
    @Override
    public PagingResponseDto<BoardResponseDto> findAllBoards(String createdAt, Long authorId, Paging paging) {
        StringBuilder countSql = new StringBuilder(QUERY_SELECT_ALL_COUNT.getSql());
        List<Object> countParams = new ArrayList<>();

        if (authorId != null) {
            countSql.append(QUERY_WHERE_AUTHOR_ID.getSql());
            countParams.add(authorId);
        }

        if (createdAt != null) {
            countSql.append(QUERY_WHERE_CREATED_AT.getSql());
            countParams.add(createdAt);
        }

        Long totalItems = jdbcTemplate.queryForObject(countSql.toString(), countParams.toArray(), Long.class);

        // 데이터 조회
        StringBuilder dataSql = new StringBuilder(QUERY_FIND_ALL.getSql());
        List<Object> dataParams = new ArrayList<>();

        if (authorId != null) {
            dataSql.append(QUERY_WHERE_AUTHOR_ID.getSql());
            dataParams.add(authorId);
        }

        if (createdAt != null) {
            dataSql.append(QUERY_WHERE_CREATED_AT.getSql());
            dataParams.add(createdAt);
        }

        dataSql.append(QUERY_ORDER_BY_CREATE_AT_AND_USE_PAGINATION.getSql());
        dataParams.add(paging.getSize());
        dataParams.add(paging.getOffset());

        List<BoardResponseDto> items = jdbcTemplate.query(dataSql.toString(), dataParams.toArray(), boardResponseRowMapper());
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
        List<BoardPasswordResponseDto> result = jdbcTemplate.query(QUERY_FIND_BY_ID.getSql(), boardRowMapper(), id);
        return result.stream().findAny().orElseThrow(() -> new NotFoundException("해당 아이디의 게시글이 존재하지 않습니다 id = " + id));
    }

    @Transactional
    @Override
    public int updateBoard(Long id, String title, String contents) {
        return jdbcTemplate.update(QUERY_UPDATE.getSql(), title, contents, id);
    }

    @Transactional
    @Override
    public int deleteBoard(Long id) {
        return jdbcTemplate.update(QUERY_DELETE.getSql(), id);
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

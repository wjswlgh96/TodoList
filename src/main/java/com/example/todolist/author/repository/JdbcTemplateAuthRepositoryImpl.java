package com.example.todolist.author.repository;

import com.example.todolist.author.dto.AuthorResponseDto;
import com.example.todolist.author.entity.Author;
import com.example.todolist.author.enums.AuthorColumn;
import com.example.todolist.author.enums.AuthorSQL;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.todolist.author.enums.AuthorSQL.*;

@Repository
public class JdbcTemplateAuthRepositoryImpl implements AuthorRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateAuthRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    @Override
    public AuthorResponseDto saveAuthor(Author author) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("author")
                        .usingColumns(
                                AuthorColumn.NAME.getColumnName(),
                                AuthorColumn.EMAIL.getColumnName()
                        )      // 사용할 컬럼을 명확히 지정해주지 않아 created_at과 updated_at이 NULL이 되어버린 문제 - Trouble
                        .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(AuthorColumn.NAME.getColumnName(), author.getName());
        parameters.put(AuthorColumn.EMAIL.getColumnName(), author.getEmail());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        Author insertedAuthor = findAuthorByIdOrElseThrow(key.longValue());

        return new AuthorResponseDto(
                insertedAuthor.getId(),
                insertedAuthor.getName(),
                insertedAuthor.getEmail(),
                insertedAuthor.getCreatedAt(),
                insertedAuthor.getUpdatedAt()
        );
    }

    @Override
    public List<AuthorResponseDto> findAllAuthors() {
        return jdbcTemplate.query(QUERY_FIND_ALL.getSql(), authorResponseDtoRowMapper());
    }

    @Override
    public Author findAuthorByIdOrElseThrow(Long id) {
        List<Author> result = jdbcTemplate.query(QUERY_FIND_BY_ID.getSql(), authorRowMapper(), id);
        return result.stream().findAny().orElseThrow(() -> new NotFoundException("해당 아이디의 작성자가 존재하지 않습니다 id = " + id));
    }

    @Transactional
    @Override
    public int updateAuthor(Long id, String name, String email) {
        return jdbcTemplate.update(QUERY_UPDATE.getSql(), name, email, id);
    }

    @Transactional
    @Override
    public int deleteAuthor(Long id) {
        return jdbcTemplate.update(QUERY_DELETE.getSql(), id);
    }

    private RowMapper<Author> authorRowMapper() {
        return new RowMapper<Author>() {
            @Override
            public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Author(
                        rs.getLong(AuthorColumn.ID.getColumnName()),
                        rs.getString(AuthorColumn.NAME.getColumnName()),
                        rs.getString(AuthorColumn.EMAIL.getColumnName()),
                        rs.getTimestamp(AuthorColumn.CREATED_AT.getColumnName()).toLocalDateTime(),
                        rs.getTimestamp(AuthorColumn.UPDATED_AT.getColumnName()).toLocalDateTime()
                );
            }
        };
    }

    private RowMapper<AuthorResponseDto> authorResponseDtoRowMapper() {
        return new RowMapper<AuthorResponseDto>() {
            @Override
            public AuthorResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new AuthorResponseDto(
                        rs.getLong(AuthorColumn.ID.getColumnName()),
                        rs.getString(AuthorColumn.NAME.getColumnName()),
                        rs.getString(AuthorColumn.EMAIL.getColumnName()),
                        rs.getTimestamp(AuthorColumn.CREATED_AT.getColumnName()).toLocalDateTime(),
                        rs.getTimestamp(AuthorColumn.UPDATED_AT.getColumnName()).toLocalDateTime()
                );
            }
        };
    }
}

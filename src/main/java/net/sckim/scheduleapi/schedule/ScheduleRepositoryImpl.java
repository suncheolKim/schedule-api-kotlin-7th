package net.sckim.scheduleapi.schedule;

import net.sckim.scheduleapi.schedule.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Schedule save(Schedule schedule) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withCatalogName("sparta").withTableName("schedule").usingGeneratedKeyColumns("id");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", schedule.getName());
        parameters.put("contents", schedule.getContents());
        parameters.put("password", schedule.getPassword());
        parameters.put("created_at", schedule.getCreatedAt());
        parameters.put("updated_at", schedule.getUpdatedAt());

        final Number number = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        schedule.setId(number.longValue());

        return schedule;
    }

    @Override
    public Optional<Schedule> findById(Long scheduleId) {
        final List<Schedule> results = jdbcTemplate.query("select * from schedule where id = ?", scheduleRowMapper(), scheduleId);

        return results.stream().findAny();
    }

    private RowMapper<Schedule> scheduleRowMapper() {
        return new RowMapper<>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                final Schedule schedule = new Schedule(
                        rs.getString("contents"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getObject("created_at", LocalDateTime.class),
                        rs.getObject("updated_at", LocalDateTime.class)
                );

                schedule.setId(rs.getLong("id"));

                return schedule;
            }
        };
    }
}
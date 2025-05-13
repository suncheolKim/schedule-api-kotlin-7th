package net.sckim.scheduleapi.schedule;

import net.sckim.scheduleapi.schedule.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {
    // 보통 Template는 필요한 1개만 사용하는 것이 일반적이나,
    // 이 2개의 차이를 보여주기 위해 jdbcTemplate, nameParameter를 둘 다 선언
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ScheduleRepositoryImpl(DataSource dataSource, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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

    /**
     * 일정 다건 조회
     * @param updatedDate 수정일
     * @param writer 작성자
     * @return 일정 리스트
     */
    @Override
    public List<Schedule> findAllBy(LocalDate updatedDate, String writer) {
        // 조회 조건 처리
        final List<String> conditions = new ArrayList<>();
        final Map<String, Object> params = new HashMap<>();

        if (updatedDate != null) {
            // 예) updatedDate가 2025-05-01 이라면
            // start = 2025-05-01 00:00:00.000
            // end   = 2025-05-02 00:00:00.000
            final LocalDateTime start = LocalDateTime.of(updatedDate, LocalTime.of(0, 0, 0, 0));
            final LocalDateTime end = LocalDateTime.of(updatedDate.plusDays(1), LocalTime.of(0, 0, 0, 0));

            conditions.add("updated_at >= :start");
            conditions.add("updated_at < :end");
            params.put("start", start);
            params.put("end", end);
        }

        if (writer != null) {
            conditions.add("name = :writer");
            params.put("writer", writer);
        }
        // end

        // 쿼리 문자열 생성
        final StringBuilder qry = new StringBuilder("SELECT * FROM schedule");

        if (!conditions.isEmpty()) {
            final String conditionString = String.join(" AND ", conditions);
            qry.append(" WHERE ").append(conditionString);
        }
        // end

        return namedParameterJdbcTemplate.query(qry.toString(), params, scheduleRowMapper());
    }

    @Override
    public int update(Schedule schedule) {
        return jdbcTemplate.update("UPDATE schedule set contents = ?, name = ?, updated_at = NOW() WHERE id = ?",
                                                        schedule.getContents(), schedule.getName(), schedule.getId());
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
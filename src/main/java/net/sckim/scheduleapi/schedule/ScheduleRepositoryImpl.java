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
        parameters.put("userId", schedule.getUserId());
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
     * @param userId      작성자 ID
     * @return 일정 리스트
     */
    @Override
    public List<Schedule> findAllBy(LocalDate updatedDate, Long userId) {
        final Map<String, Object> params = new HashMap<>();
        final String qry = generateQueryOfSelectAll(updatedDate, userId, params);

        return namedParameterJdbcTemplate.query(qry, params, scheduleRowMapper());
    }

    @Override
    public int update(Schedule schedule) {
        return jdbcTemplate.update("UPDATE schedule set contents = ?, updated_at = NOW() WHERE id = ?",
                                                        schedule.getContents(), schedule.getId());
    }

    @Override
    public int deleteById(Long scheduleId) {
        return jdbcTemplate.update("DELETE FROM schedule WHERE id = ?", scheduleId);
    }

    /**
     * 페이징 조회
     * @param updatedDate 수정일
     * @param userId 사용자 ID
     * @param page 페이지 번호 (디폴트 1)
     * @param size 사이즈 (디폴트 10)
     * @return 페이지에 해당하는 일정 리스트
     */
    @Override
    public List<Schedule> findPageBy(LocalDate updatedDate, Long userId, Integer page, Integer size) {
        final Map<String, Object> params = new HashMap<>();
        final String qry = generateQueryOfSelectAll(updatedDate, userId, page, size, params);

        return namedParameterJdbcTemplate.query(qry, params, scheduleRowMapper());
    }

    @Override
    public long countAllBy(LocalDate updatedDate, Long userId) {
        final List<Schedule> scheduleList = findAllBy(updatedDate, userId);
        return scheduleList.size();
    }

    private String generateQueryOfSelectAll(LocalDate updatedDate, Long userId, Integer page, Integer size, Map<String, Object> params) {
        final String qry = generateQueryOfSelectAll(updatedDate, userId, params);

        if (page == null && size == null) {
            page = 1;
        }
        if (size == null) {
            size = 10;
        }

        final int offset = (page - 1) * size;
        return qry + " LIMIT " + size + " OFFSET " + offset;
    }

    private String generateQueryOfSelectAll(LocalDate updatedDate, Long userId, Map<String, Object> params) {
        // 조회 조건 처리
        final List<String> conditions = new ArrayList<>();

        if (updatedDate != null) {
            // 예) updatedDate가 2025-05-01 이라면
            // start = 2025-05-01 00:00:00.000
            // end   = 2025-05-02 00:00:00.000
            final LocalDateTime start = LocalDateTime.of(updatedDate, LocalTime.of(0, 0, 0, 0));
            final LocalDateTime end = LocalDateTime.of(updatedDate.plusDays(1), LocalTime.of(0, 0, 0, 0));

            conditions.add("s.updated_at >= :start");
            conditions.add("s.updated_at < :end");
            params.put("start", start);
            params.put("end", end);
        }

        if (userId != null) {
            conditions.add("s.user_id = :userId");
            params.put("userId", userId);
        }
        // end

        // 쿼리 문자열 생성
        final StringBuilder qry = new StringBuilder("SELECT * FROM schedule s" +
                "                                             JOIN user u ON s.user_id = u.id");

        if (!conditions.isEmpty()) {
            final String conditionString = String.join(" AND ", conditions);
            qry.append(" WHERE ").append(conditionString);
        }
        // end

        return qry.toString();
    }

    private RowMapper<Schedule> scheduleRowMapper() {
        return new RowMapper<>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                final Schedule schedule = new Schedule(
                        rs.getLong("user_id"),
                        rs.getString("contents"),
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
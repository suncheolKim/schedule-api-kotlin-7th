package net.sckim.scheduleapi.user;

import net.sckim.scheduleapi.user.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findById(Long userId) {
        final List<User> results = jdbcTemplate.query("select * from user where id = ?", userRowMapper(), userId);

        return results.stream().findAny();
    }

    @Override
    public int update(User user) {
        return jdbcTemplate.update("UPDATE user set name = ?, updated_at = NOW() WHERE id = ?",
                user.getName(), user.getId());
    }

    private RowMapper<User> userRowMapper() {
        return new RowMapper<>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                final User user = new User(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getObject("created_at", LocalDateTime.class),
                        rs.getObject("updated_at", LocalDateTime.class)
                );

                user.setId(rs.getLong("id"));

                return user;
            }
        };
    }
}

package ru.dzhenbaz.P2PTransactionsTask.dao.mapper;

import ru.dzhenbaz.P2PTransactionsTask.model.User;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@link RowMapper} для преобразования строки из таблицы {@code users}
 * в объект {@link User}.
 * <p>
 * Используется в реализации {@code UserDao} при выполнении SQL-запросов через {@code JdbcTemplate}.
 * </p>
 *
 * <p><b>Примечание:</b> поле {@code created_at} преобразуется в {@code LocalDateTime}.</p>
 *
 * @author Dzhenbaz
 */
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return user;
    }
}

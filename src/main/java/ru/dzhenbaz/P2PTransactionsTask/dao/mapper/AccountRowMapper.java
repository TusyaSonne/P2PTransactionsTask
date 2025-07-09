package ru.dzhenbaz.P2PTransactionsTask.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.dzhenbaz.P2PTransactionsTask.model.Account;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@link RowMapper} для преобразования строки из таблицы {@code account}
 * в объект {@link Account}.
 * <p>
 * Используется в реализации {@code AccountDao} при выполнении SQL-запросов с {@code JdbcTemplate}.
 * </p>
 *
 * <p><b>Примечание:</b> поле {@code balance} интерпретируется как {@code Long} в копейках,
 * поле {@code created_at} преобразуется в {@code LocalDateTime}.</p>
 *
 * @author Dzhenbaz
 */
public class AccountRowMapper implements RowMapper<Account> {
    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        Account account = new Account();
        account.setId(rs.getLong("id"));
        account.setUserId(rs.getLong("user_id"));
        account.setBalance(rs.getLong("balance"));
        account.setClosed(rs.getBoolean("is_closed"));
        account.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return account;
    }
}

package ru.dzhenbaz.P2PTransactionsTask.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.dzhenbaz.P2PTransactionsTask.model.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@link RowMapper} для преобразования строки из таблицы {@code transaction}
 * в объект {@link Transaction}.
 * <p>
 * Используется в реализации {@code TransactionDao} при работе с {@code JdbcTemplate}.
 * </p>
 *
 * <p><b>Примечание:</b> поле {@code amount} указывается в копейках, а {@code created_at}
 * преобразуется в {@code LocalDateTime}.</p>
 *
 * @author Dzhenbaz
 */
public class TransactionRowMapper implements RowMapper<Transaction> {

    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        Transaction tx = new Transaction();
        tx.setId(rs.getLong("id"));
        tx.setFromAccountId(rs.getLong("from_account_id"));
        tx.setToAccountId(rs.getLong("to_account_id"));
        tx.setAmount(rs.getLong("amount"));
        tx.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return tx;
    }
}

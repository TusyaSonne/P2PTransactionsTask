package ru.dzhenbaz.P2PTransactionsTask.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.dzhenbaz.P2PTransactionsTask.model.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

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

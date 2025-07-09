package ru.dzhenbaz.P2PTransactionsTask.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.dzhenbaz.P2PTransactionsTask.dao.TransactionDao;
import ru.dzhenbaz.P2PTransactionsTask.dao.mapper.TransactionRowMapper;
import ru.dzhenbaz.P2PTransactionsTask.model.Transaction;

import java.util.List;

@Repository
public class TransactionDaoImpl implements TransactionDao {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionRowMapper rowMapper = new TransactionRowMapper();

    @Autowired
    public TransactionDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Transaction tx) {
        jdbcTemplate.update(
                "INSERT INTO transactions (from_account_id, to_account_id, amount, created_at) VALUES (?, ?, ?, ?)",
                tx.getFromAccountId(),
                tx.getToAccountId(),
                tx.getAmount(),
                tx.getCreatedAt()
        );
    }

    @Override
    public List<Transaction> findAllByAccountId(Long accountId) {
        return jdbcTemplate.query(
                "SELECT * FROM transactions WHERE from_account_id = ? OR to_account_id = ? ORDER BY created_at DESC",
                rowMapper,
                accountId,
                accountId
        );
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM transactions");
    }
}

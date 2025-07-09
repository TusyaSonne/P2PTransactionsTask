package ru.dzhenbaz.P2PTransactionsTask.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.dzhenbaz.P2PTransactionsTask.dao.AccountDao;
import ru.dzhenbaz.P2PTransactionsTask.dao.mapper.AccountRowMapper;
import ru.dzhenbaz.P2PTransactionsTask.model.Account;

import java.util.List;
import java.util.Optional;

@Repository
public class AccountDaoImpl implements AccountDao {

    private final JdbcTemplate jdbcTemplate;
    private final AccountRowMapper rowMapper = new AccountRowMapper();

    @Autowired
    public AccountDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Account account) {
        jdbcTemplate.update(
                "INSERT INTO accounts (user_id, balance, is_closed, created_at) VALUES (?, ?, ?, ?)",
                account.getUserId(),
                account.getBalance(),
                account.isClosed(),
                account.getCreatedAt()
        );
    }

    @Override
    public Optional<Account> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM accounts WHERE id = ?", rowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Account> findByUserId(Long userId) {
        return jdbcTemplate.query("SELECT * FROM accounts WHERE user_id = ? AND is_closed = false", rowMapper, userId);
    }

    @Override
    public void updateBalance(Long accountId, Long newBalance) {
        jdbcTemplate.update("UPDATE accounts SET balance = ? WHERE id = ?", newBalance, accountId);
    }

    @Override
    public void closeAccount(Long accountId) {
        jdbcTemplate.update("UPDATE accounts SET is_closed = true WHERE id = ?", accountId);
    }

    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM accounts");
    }
}

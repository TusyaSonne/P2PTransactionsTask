package ru.dzhenbaz.P2PTransactionsTask.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.dzhenbaz.P2PTransactionsTask.dao.UserDao;
import ru.dzhenbaz.P2PTransactionsTask.dao.mapper.UserRowMapper;
import ru.dzhenbaz.P2PTransactionsTask.model.User;

import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper rowMapper = new UserRowMapper();

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update(
                "INSERT INTO users (username, password, created_at) VALUES (?, ?, ?)",
                user.getUsername(),
                user.getPassword(),
                user.getCreatedAt()
        );
    }

    @Override
    public Optional<User> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ?", rowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?", rowMapper, username)
                .stream()
                .findFirst();
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM users");
    }
}

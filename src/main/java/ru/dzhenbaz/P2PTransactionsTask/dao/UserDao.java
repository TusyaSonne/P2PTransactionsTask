package ru.dzhenbaz.P2PTransactionsTask.dao;

import ru.dzhenbaz.P2PTransactionsTask.model.User;

import java.util.Optional;

public interface UserDao {

    void save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
}

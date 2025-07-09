package ru.dzhenbaz.P2PTransactionsTask.dao;

import ru.dzhenbaz.P2PTransactionsTask.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDao {

    void save(Account account);
    Optional<Account> findById(Long id);
    List<Account> findByUserId(Long userId);
    void updateBalance(Long accountId, Long newBalance);
    void closeAccount(Long accountId);
}

package ru.dzhenbaz.P2PTransactionsTask.dao;

import ru.dzhenbaz.P2PTransactionsTask.model.Transaction;

import java.util.List;

public interface TransactionDao {

    void save(Transaction tx);
    List<Transaction> findAllByAccountId(Long accountId);
}

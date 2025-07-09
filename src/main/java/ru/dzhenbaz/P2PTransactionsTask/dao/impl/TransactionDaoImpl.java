package ru.dzhenbaz.P2PTransactionsTask.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.dzhenbaz.P2PTransactionsTask.dao.TransactionDao;
import ru.dzhenbaz.P2PTransactionsTask.dao.mapper.TransactionRowMapper;
import ru.dzhenbaz.P2PTransactionsTask.model.Transaction;

import java.util.List;

/**
 * Реализация интерфейса {@link TransactionDao} с использованием {@link JdbcTemplate}.
 * <p>
 * Обеспечивает взаимодействие с таблицей {@code transactions} в базе данных:
 * сохранение транзакций, получение всех переводов по счёту, удаление всех записей.
 * </p>
 *
 * <p>Маппинг выполняется с помощью {@link TransactionRowMapper}.</p>
 *
 * @author Dzhenbaz
 */
@Repository
public class TransactionDaoImpl implements TransactionDao {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionRowMapper rowMapper = new TransactionRowMapper();

    /**
     * Конструктор с внедрением зависимости {@link JdbcTemplate}.
     *
     * @param jdbcTemplate компонент для выполнения SQL-запросов
     */
    @Autowired
    public TransactionDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Сохраняет новую транзакцию в базе данных.
     *
     * @param tx объект транзакции
     */
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

    /**
     * Возвращает список всех транзакций, связанных с указанным счётом (входящих и исходящих).
     * <p>Результат отсортирован по дате создания в порядке убывания (последние первыми).</p>
     *
     * @param accountId идентификатор счёта
     * @return список транзакций по счёту
     */
    @Override
    public List<Transaction> findAllByAccountId(Long accountId) {
        return jdbcTemplate.query(
                "SELECT * FROM transactions WHERE from_account_id = ? OR to_account_id = ? ORDER BY created_at DESC",
                rowMapper,
                accountId,
                accountId
        );
    }

    /**
     * Удаляет все транзакции из таблицы.
     * Используется в тестовой или отладочной среде.
     */
    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM transactions");
    }
}

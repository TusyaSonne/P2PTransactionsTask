package ru.dzhenbaz.P2PTransactionsTask.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.dzhenbaz.P2PTransactionsTask.dao.AccountDao;
import ru.dzhenbaz.P2PTransactionsTask.dao.mapper.AccountRowMapper;
import ru.dzhenbaz.P2PTransactionsTask.model.Account;

import java.util.List;
import java.util.Optional;

/**
 * Реализация интерфейса {@link AccountDao} с использованием {@link JdbcTemplate}.
 * <p>
 * Предоставляет доступ к таблице {@code accounts} в базе данных:
 * создание счёта, обновление баланса, поиск по ID, закрытие и выборка по пользователю.
 * </p>
 *
 * <p>Использует {@link AccountRowMapper} для маппинга строк БД в объекты {@link Account}.</p>
 *
 * @author Dzhenbaz
 */
@Repository
public class AccountDaoImpl implements AccountDao {

    private final JdbcTemplate jdbcTemplate;
    private final AccountRowMapper rowMapper = new AccountRowMapper();

    /**
     * Конструктор с внедрением зависимости {@link JdbcTemplate}.
     *
     * @param jdbcTemplate компонент для взаимодействия с БД
     */
    @Autowired
    public AccountDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Сохраняет новый счёт в базе данных.
     *
     * @param account объект счёта для сохранения
     */
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

    /**
     * Находит счёт по его идентификатору.
     *
     * @param id идентификатор счёта
     * @return {@code Optional} с найденным счётом или пустой, если не найден
     */
    @Override
    public Optional<Account> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM accounts WHERE id = ?", rowMapper, id)
                .stream()
                .findFirst();
    }

    /**
     * Возвращает все активные (не закрытые) счета пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список счетов пользователя
     */
    @Override
    public List<Account> findByUserId(Long userId) {
        return jdbcTemplate.query("SELECT * FROM accounts WHERE user_id = ? AND is_closed = false", rowMapper, userId);
    }

    /**
     * Обновляет баланс указанного счёта.
     *
     * @param accountId идентификатор счёта
     * @param newBalance новый баланс в копейках
     */
    @Override
    public void updateBalance(Long accountId, Long newBalance) {
        jdbcTemplate.update("UPDATE accounts SET balance = ? WHERE id = ?", newBalance, accountId);
    }

    /**
     * Помечает счёт как закрытый.
     *
     * @param accountId идентификатор счёта
     */
    @Override
    public void closeAccount(Long accountId) {
        jdbcTemplate.update("UPDATE accounts SET is_closed = true WHERE id = ?", accountId);
    }

    /**
     * Удаляет все счета из таблицы.
     * Используется, как правило, в тестах.
     */
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM accounts");
    }
}

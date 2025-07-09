package ru.dzhenbaz.P2PTransactionsTask.dao;

import ru.dzhenbaz.P2PTransactionsTask.model.Account;

import java.util.List;
import java.util.Optional;

/**
 * DAO-интерфейс для работы с банковскими счетами пользователей.
 * <p>
 * Определяет методы для создания, получения, обновления и закрытия счетов.
 * Используется реализация на основе {@code JdbcTemplate},
 * согласно требованиям задания (без JPA).
 * </p>
 *
 * @author Dzhenbaz
 */
public interface AccountDao {

    /**
     * Сохраняет новый счёт в базе данных.
     *
     * @param account объект счёта для сохранения
     */
    void save(Account account);

    /**
     * Находит счёт по его идентификатору.
     *
     * @param id идентификатор счёта
     * @return {@code Optional} с найденным счётом или пустой, если не найден
     */
    Optional<Account> findById(Long id);

    /**
     * Возвращает список счетов, принадлежащих пользователю.
     *
     * @param userId идентификатор пользователя
     * @return список счетов пользователя
     */
    List<Account> findByUserId(Long userId);

    /**
     * Обновляет баланс счёта.
     *
     * @param accountId идентификатор счёта
     * @param newBalance новый баланс в копейках
     */
    void updateBalance(Long accountId, Long newBalance);

    /**
     * Помечает счёт как закрытый.
     *
     * @param accountId идентификатор счёта
     */
    void closeAccount(Long accountId);

    /**
     * Удаляет все счета из базы данных.
     * <p>Используется преимущественно в тестах.</p>
     */
    void deleteAll();
}

package ru.dzhenbaz.P2PTransactionsTask.dao;

import ru.dzhenbaz.P2PTransactionsTask.model.Transaction;

import java.util.List;

/**
 * DAO-интерфейс для работы с транзакциями.
 * <p>
 * Определяет методы для сохранения и получения информации о переводах между счетами.
 * Используется реализация на основе {@code JdbcTemplate} в соответствии с техническими ограничениями.
 * </p>
 *
 * @author Dzhenbaz
 */
public interface TransactionDao {

    /**
     * Сохраняет новую транзакцию в базе данных.
     *
     * @param tx объект транзакции для сохранения
     */
    void save(Transaction tx);

    /**
     * Возвращает все транзакции, связанные с указанным счётом.
     * Не используется, возможно понадобится при расширении
     * <p>Сюда входят как исходящие, так и входящие переводы.</p>
     *
     * @param accountId идентификатор счёта
     * @return список транзакций по данному счёту
     */
    List<Transaction> findAllByAccountId(Long accountId);

    /**
     * Удаляет все транзакции из базы данных.
     * <p>Как правило, используется в тестах или при очистке среды.</p>
     */
    void deleteAll();
}

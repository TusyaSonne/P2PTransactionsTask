package ru.dzhenbaz.P2PTransactionsTask.dao;

import ru.dzhenbaz.P2PTransactionsTask.model.User;

import java.util.Optional;

/**
 * DAO-интерфейс для работы с пользователями.
 * <p>
 * Определяет базовые операции для сохранения и получения информации о пользователях.
 * Используется {@code JdbcTemplate} вместо JPA, согласно техническим требованиям.
 * </p>
 *
 * @author Dzhenbaz
 */
public interface UserDao {

    /**
     * Сохраняет нового пользователя в базе данных.
     *
     * @param user объект пользователя для сохранения
     */
    void save(User user);

    /**
     * Находит пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return {@code Optional} с найденным пользователем или пустой, если не найден
     */
    Optional<User> findById(Long id);

    /**
     * Находит пользователя по имени пользователя (логину).
     *
     * @param username имя пользователя
     * @return {@code Optional} с найденным пользователем или пустой, если не найден
     */
    Optional<User> findByUsername(String username);

    /**
     * Удаляет всех пользователей из базы данных.
     * <p>Обычно используется в тестовой среде.</p>
     */
    void deleteAll();
}

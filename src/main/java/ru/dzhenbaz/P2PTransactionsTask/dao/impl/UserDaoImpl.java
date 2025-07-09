package ru.dzhenbaz.P2PTransactionsTask.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.dzhenbaz.P2PTransactionsTask.dao.UserDao;
import ru.dzhenbaz.P2PTransactionsTask.dao.mapper.UserRowMapper;
import ru.dzhenbaz.P2PTransactionsTask.model.User;

import java.util.Optional;

/**
 * Реализация интерфейса {@link UserDao} с использованием {@link JdbcTemplate}.
 * <p>
 * Обеспечивает доступ к таблице {@code users} в базе данных:
 * сохранение, поиск по ID и username, удаление всех записей.
 * </p>
 *
 * <p>Использует {@link UserRowMapper} для преобразования строк в объекты {@link User}.</p>
 *
 * @author Dzhenbaz
 */
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper rowMapper = new UserRowMapper();

    /**
     * Конструктор с внедрением зависимости {@link JdbcTemplate}.
     *
     * @param jdbcTemplate компонент для взаимодействия с БД
     */
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Сохраняет нового пользователя в базу данных.
     *
     * @param user пользователь для сохранения
     */
    @Override
    public void save(User user) {
        jdbcTemplate.update(
                "INSERT INTO users (username, password, created_at) VALUES (?, ?, ?)",
                user.getUsername(),
                user.getPassword(),
                user.getCreatedAt()
        );
    }

    /**
     * Ищет пользователя по ID.
     *
     * @param id идентификатор пользователя
     * @return {@code Optional} с пользователем или пустой, если не найден
     */
    @Override
    public Optional<User> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ?", rowMapper, id)
                .stream()
                .findFirst();
    }

    /**
     * Ищет пользователя по логину.
     *
     * @param username имя пользователя
     * @return {@code Optional} с пользователем или пустой, если не найден
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?", rowMapper, username)
                .stream()
                .findFirst();
    }

    /**
     * Удаляет всех пользователей из таблицы.
     * Обычно используется в тестовой среде.
     */
    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM users");
    }
}

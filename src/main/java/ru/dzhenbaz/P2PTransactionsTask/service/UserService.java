package ru.dzhenbaz.P2PTransactionsTask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dzhenbaz.P2PTransactionsTask.dao.UserDao;
import ru.dzhenbaz.P2PTransactionsTask.model.User;
import java.util.Optional;

/**
 * Сервис для работы с пользователями.
 * <p>
 * Предоставляет доступ к данным пользователей через {@link UserDao}.
 * Используется в слоях бизнес-логики и контроллерах.
 * </p>
 *
 * @author Dzhenbaz
 */
@Service
public class UserService {

    private final UserDao userDao;

    /**
     * Конструктор с внедрением {@link UserDao}.
     *
     * @param userDao DAO для работы с пользователями
     */
    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Ищет пользователя по имени пользователя (логину).
     *
     * @param username логин пользователя
     * @return {@code Optional} с пользователем, если найден
     */
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /**
     * Ищет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return {@code Optional} с пользователем, если найден
     */
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }
}
